package com.dailymission.api.springboot.web.service.post;

import com.dailymission.api.springboot.exception.ResourceNotFoundException;
import com.dailymission.api.springboot.security.UserPrincipal;
import com.dailymission.api.springboot.web.dto.post.*;
import com.dailymission.api.springboot.web.dto.rabbitmq.MessageDto;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.MissionRepository;
import com.dailymission.api.springboot.web.repository.participant.Participant;
import com.dailymission.api.springboot.web.repository.participant.ParticipantRepository;
import com.dailymission.api.springboot.web.repository.post.Post;
import com.dailymission.api.springboot.web.repository.post.PostRepository;
import com.dailymission.api.springboot.web.repository.user.User;
import com.dailymission.api.springboot.web.repository.user.UserRepository;
import com.dailymission.api.springboot.web.service.image.ImageService;
import com.dailymission.api.springboot.web.service.rabbitmq.MessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {
    // service
    private final ImageService imageService;

    // repository
    private final PostRepository postRepository;
    private final MissionRepository missionRepository;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;

    // message producer
    private final MessageProducer messageProducer;

    /**
     * [ 2020-03-13 : 이민호 ]
     * 설명 : 포스트를 저장한다.
     * */
    @Transactional
    public Long save(PostSaveRequestDto requestDto, UserPrincipal userPrincipal) throws IOException {
        // user
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

        // mission
        Mission mission = missionRepository.findById(requestDto.getMissionId())
                .orElseThrow(() -> new ResourceNotFoundException("Mission", "id", requestDto.getMissionId()));

        // participant
        Participant participant = participantRepository.findByMissionAndUser(mission, user)
                .orElseThrow(()-> new NoSuchElementException("미션에 참여중인 사용자가 아닙니다."));

        // 강퇴여부 확인
        if(participant.isBanned()){
            throw new IllegalAccessError("당신은 강퇴당하셨습니다.");
        }

        // 미션 종료 및 삭제여부 확인
        if(mission.isEnded() || mission.isDeleted()){
            throw new IllegalArgumentException("참여가능한 미션이 아닙니다.");
        }

        // 제출 기록 확인
        LocalDateTime now = LocalDateTime.now();
        boolean isSubmit = isSubmitToday(participant, now);


        // 이미 제출한 기록이 있을 경우
        if(isSubmit){
            throw new IllegalArgumentException("금일 인증을 완료한 미션입니다.");
        }


        /**
         * [ 2020-03-23 : 이민호 ]
         * 설명 : POST 객체 변환시 @Column 어노테이션이 TITLE/CONTENT 의 Null 을 검사한다.
         * */
        // entity
        Post post = requestDto.toEntity(user, mission);

        // upload image
        MessageDto message = imageService.uploadPostS3(requestDto.getFile(), mission.getTitle());
        post.updateImage(message.getImageUrl());

        // create post
        post = postRepository.save(post);

        // produce message
        messageProducer.sendMessage(user, mission, post, message);

        // return post id
        return post.getId();
    }

    /**
     * [ 2020-03-13 : 이민호 ]
     * 설명 : 포스트의 Detail 정보를 가져온다.
     * */
    @Transactional(readOnly = true)
    public PostResponseDto findById (Long id) throws Exception {

        // get post if not deleted
        Post post = postRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new NoSuchElementException("해당 게시글이 없습니다. id=" + id));

        return new PostResponseDto(post);
    }

    /**
     * [ 2020-03-13 : 이민호 ]
     * 설명 : 전체 포스트 목록을 가져온다.
     * */
    @Transactional(readOnly = true)
    public List<PostListResponseDto> findAll(){

        // get all posts
        return postRepository.findAll().stream()
                .map(PostListResponseDto::new)
                .collect(Collectors.toList());

    }

    /**
     * [ 2020-03-13 : 이민호 ]
     * 설명 : 유저별 전체 포스트 목록을 가져온다.
     * */
    @Transactional(readOnly = true)
    public List<PostListResponseDto> findAllByUser(UserPrincipal userPrincipal){

        // user
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

        // get all posts by user
        return postRepository.findAllByUser(user).stream()
                .map(PostListResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * [ 2020-03-13 : 이민호 ]
     * 설명 : 미션별 전체 포스트 목록을 가져온다.
     * */
    @Transactional(readOnly = true)
    public List<PostListResponseDto> findAllByMission(Long id){

        // mission
        Mission mission = missionRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mission", "id", id));

        // get all posts by mission
        return postRepository.findAllByMission(mission).stream()
                .map(PostListResponseDto::new)
                .collect(Collectors.toList());
    }


    /**
     * [ 2020-03-13 : 이민호 ]
     * 설명 : 미션별로 해당 Week 의 post 제출 schedule 을 가져온다.
     *        week : 0 -> 이번주
     *        week : n -> n 주전
     *
     *        일/월/화/수/목/금/토 정보를 가져온다.
     * */
    @Transactional(readOnly = true)
    public PostScheduleResponseDto findSchedule(Long id, Long week){

        /**
         * [ 2020-03-13 : 이민호 ]
         * 설명 : 현재 요일에서 가장 가까운 일요일을 찾는다. (previousOrSame)
         *        ex) 월 -> 지난 일요일
         *        ex) 토 -> 지난 일요일
         *        ex) 일 -> 현재 일요일
         * */
        LocalDate startDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).minusWeeks(week);


        // mission
        Mission mission = missionRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mission", "id", id));


        /**
         * [ 2020-03-13 : 이민호 ]
         * 설명 : week 주의 일주일간 날짜 + 제출의무 요일인지 확인
         *      ex) 메서드 호출 일자 : 2020-03-13  / week : 0
         *          -> 2020-03-08 ~ 2020-03-14
         *          -> true/true/true/true/true/false/false
         *  */
        List<DateDto> weekDates = mission.getWeekDates(startDate);


        /**
         * [ 2020-03-13 : 이민호 ]
         * 설명 : 미션별 weekly post submit 을 PostSubmitDto 객체로 전달받는다.
         *        0시 ~ 03 시 제출 기록은 이전날짜 제출 기록으로 변환한다.
         * */
        List<PostSubmitDto> submits = postRepository.findWeeklyPostSubmitByMission(id, startDate);


        /**
         * [ 2020-03-13 : 이민호 ]
         * 설명 : 전달받은 PostSubmitDto 객체를 조합해 유저별로 묶는다.
         *        결과값 : 유저정보 + 제출일자 리스트 {'2020-03-08', '2020-03-14'...}
         *        ## submit 정보를 활용해 -> history 로 의미있게 만든다..
         * */
        List<PostHistoryDto> histories = mission.getHistories(submits);


        /**
         * [ 2020-03-13 : 이민호 ]
         * 설명 : Schedule > histories > submits
         * */
        return  PostScheduleResponseDto.builder()
                                        .weekDates(weekDates)
                                        .histories(histories)
                                        .build();
    }




//    /**
//     * POST 업데이트
//     * */
//    @Transactional
//    public Long update(Long id, PostUpdateRequestDto requestDto){
//        Optional<Post> optional = Optional.ofNullable(postRepository.findByIdAndDeletedIsFalse(id))
//                .orElseThrow(() -> new NoSuchElementException("해당 게시글이 없습니다. id=" + id));
//
//        Post post = optional.get();
//        post.update(requestDto.getTitle(), requestDto.getContent());
//
//        return id;
//    }

//    /**
//     * POST 이미지 업데이트
//     * */
//    @Transactional
//    public Long updateImage(Long id, MultipartFile file) throws IOException {
//        Optional<Post> optional = Optional.ofNullable(postRepository.findByIdAndDeletedIsFalse(id))
//                .orElseThrow(() -> new NoSuchElementException("해당 게시글이 없습니다. id=" + id));
//
//        Post post = optional.get();
//
//        // change image
//        MessageDto message = imageService.uploadPostS3(file, post.getMission().getTitle() + imageService.getPostDir());
//        post.updateImage(message.getImageUrl());
//
//        return id;
//    }


    /**
     * [ 2020-03-13 : 이민호 ]
     * 설명 : 포스트를 삭제한다.
     * */
    @Transactional
    public void delete(Long id, UserPrincipal userPrincipal){

        // user
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

        // post
        Post post = postRepository.findByIdAndDeletedIsFalse(id)
                        .orElseThrow(()-> new ResourceNotFoundException("Post", "id" , id));

        // check is deletable
        if(post.isDeletable(user)){
            // delete flag -> 'Y'
            post.delete();
        }
    }


    /**
     * [ 2020-03-11 : 이민호 ]
     * 설명 : 참여중인 미션에 금일 인증한 포스트 제출 기록이 있는지 확인한다.
     * */
    @Transactional(readOnly = true)
    public boolean isSubmitToday(Participant participant, LocalDateTime now){
        // result
        boolean isSubmit = false;

        /**
         * [ 2020-03-11 : 이민호 ]
         * 설명 : 현재시간 0시 ~ 03시 : 전날 03시 ~ 현재시간까지 제출기록이 있는지 확인한다.
         *       현재시간 03시 ~ 24시 : 금일 03시 ~ 현재시간까지 제출기록이 있는지 확인한다.
         * */
        // 0시  ~ 03시
        LocalDateTime criteria = LocalDate.now().atTime(03,00); // 03시
        if(now.isBefore(criteria)){
            // 전날 새벽 3시 ~ 현재
            isSubmit =  postRepository.countPostSubmit(participant.getMission()
                    ,participant.getUser()
                    ,criteria.minusDays(1)
                    ,now) > 0;


        }else{
            // 03시 ~ 24시
            // 금일 새벽 3시 ~ 현재
            isSubmit =  postRepository.countPostSubmit(participant.getMission()
                    ,participant.getUser()
                    ,criteria
                    ,now) > 0;
        }

        return isSubmit;
    }
}
