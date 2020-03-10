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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {

    private final ImageService imageService;

    private final PostRepository postRepository;

    private final MissionRepository missionRepository;

    private final UserRepository userRepository;

    private final ParticipantRepository participantRepository;

    private final MessageProducer messageProducer;

    /**
     * POST 저장
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
        if(!mission.checkStatus()){
            throw new IllegalArgumentException("참여가능한 미션이 아닙니다.");
        }

        // 미션 종료 날짜 확인 (이미 종료된 미션엔 제출 불가능)
        if(!mission.checkEndDate(LocalDate.now())){
            throw new IllegalArgumentException("이미 종료된 미션입니다.");
        }

        LocalDateTime start = LocalDateTime.now();
        boolean isSubmit = false;

        // 0시  ~ 03시
        if(start.isBefore(LocalDate.now().atTime(3, 0))){
            // 전날 새벽 3시 ~ 현재
            isSubmit = findSubmitHistory(participant.getMission().getId(),
                    user.getId(),
                    LocalDate.now().atTime(3,0).minusDays(1),
                    start);
        }else{
            // 03시 ~ 24시
            // 전날 새벽 3시 ~ 현재
            isSubmit = findSubmitHistory(participant.getMission().getId(),
                    user.getId(),
                    LocalDate.now().atTime(3,0),
                    start);
        }

        // 이미 제출한 기록이 있을 경우
        if(isSubmit){
            throw new IllegalArgumentException("금일 인증을 완료한 미션입니다.");
        }

        // entity
        Post post = requestDto.toEntity(user, mission);

        // upload image
        MessageDto message = imageService.uploadPostS3(requestDto.getFile(), mission.getTitle());
        post.updateImage(message.getImageUrl());

        // create post
        post = postRepository.save(post);

        // produce message
        messageProducer.sendMessage(user, mission, post, message);

        return post.getId();
    }

    /**
     * POST 정보 (detail)
     * */
    @Transactional(readOnly = true)
    public PostResponseDto findById (Long id) throws Exception {
        Post post = postRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new NoSuchElementException("해당 게시글이 없습니다. id=" + id));

        return new PostResponseDto(post);
    }

    /**
     * 전체 POST List
     * */
    @Transactional(readOnly = true)
    public List<PostListResponseDto> findAll(){
        return postRepository.findAllDescAndDeletedIsFalse().stream()
                .map(PostListResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 유저별 전체 POST List
     * */
    @Transactional(readOnly = true)
    public List<PostListResponseDto> findAllByUser(UserPrincipal userPrincipal){
        // user
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

        return postRepository.findAllByUserEqualsAndDeletedIsFalse(user).stream()
                .map(PostListResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 미션별 전체 POST List
     * */
    @Transactional(readOnly = true)
    public List<PostListResponseDto> findAllByMission(Long id){
        // mission
        Mission mission = missionRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mission", "id", id));

        return postRepository.findAllByMissionAndDeletedIsFalse(mission).stream()
                .map(PostListResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 미션별 포스트 제출기록 (전체유저)
     * */
    @Transactional(readOnly = true)
    public PostScheduleResponseDto findSchedule(Long id, Long week){
        /**
         * 조회 시작하는 일요일 정보
         * 0 : 이번주 일요일
         * 1 : 저번주 일요일
         * */
        LocalDate startDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).minusWeeks(week);
        LocalDate endDate = startDate.plusDays(7);

        LocalDateTime startDateTime = LocalDateTime.of(startDate.getYear(),startDate.getMonth(),startDate.getDayOfMonth(), 03, 00, 00);
        LocalDateTime endDateTime = LocalDateTime.of(endDate.getYear(), endDate.getMonth(), endDate.getDayOfMonth(), 03, 00 ,00);

        // mission
        Mission mission = missionRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mission", "id", id));

        // history
        List<PostHistoryDto> historyDtoList = postRepository.findSchedule(id, startDateTime, endDateTime);


        return  PostScheduleResponseDto.builder()
                                        .startDate(startDate)
                                        .week(mission.getMissionRule().getWeek())
                                        .histories(historyDtoList)
                                        .participants(mission.getParticipants())
                                        .build();
    }

    /**
     * 유저 포스트 제출기록 (미션별)
     * */
    @Transactional(readOnly = true)
    public boolean findSubmitHistory(Long missionId, Long userId, LocalDateTime startDate, LocalDateTime endDate){

         // is submit exist?
         List<Post> submit =  postRepository.findSubmitHistory(missionId, userId, startDate, endDate);

         // true if exist
         if(submit.size()>0){
             return true;
         }else{
             return false;
         }
    }

    /**
     * POST 업데이트
     * */
    @Transactional
    public Long update(Long id, PostUpdateRequestDto requestDto){
        Optional<Post> optional = Optional.ofNullable(postRepository.findByIdAndDeletedIsFalse(id))
                .orElseThrow(() -> new NoSuchElementException("해당 게시글이 없습니다. id=" + id));

        Post post = optional.get();
        post.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    /**
     * POST 이미지 업데이트
     * */
    @Transactional
    public Long updateImage(Long id, MultipartFile file) throws IOException {
        Optional<Post> optional = Optional.ofNullable(postRepository.findByIdAndDeletedIsFalse(id))
                .orElseThrow(() -> new NoSuchElementException("해당 게시글이 없습니다. id=" + id));

        Post post = optional.get();

        // change image
        MessageDto message = imageService.uploadPostS3(file, post.getMission().getTitle() + imageService.genDir());
        post.updateImage(message.getImageUrl());

        return id;
    }


    /**
     * POST 삭제
     * */
    @Transactional
    public void delete(Long id, UserPrincipal userPrincipal){
        // user
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

        // post
        Optional<Post> optional = Optional.ofNullable(postRepository.findByIdAndDeletedIsFalse(id))
                        .orElseThrow(()-> new NoSuchElementException("해당 게시글이 없습니다. id =" + id));

        // delete flag -> 'Y'
        Post post = optional.get();
        post.delete(user);
    }

}
