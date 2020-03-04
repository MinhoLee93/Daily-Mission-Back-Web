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
import com.dailymission.api.springboot.web.repository.schedule.Schedule;
import com.dailymission.api.springboot.web.repository.user.User;
import com.dailymission.api.springboot.web.repository.user.UserRepository;
import com.dailymission.api.springboot.web.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

        // entity
        Post post = requestDto.toEntity(user, mission);

        // upload image
        MessageDto messageDto = imageService.uploadPostS3(requestDto.getFile(), mission.getTitle());
        post.updateImage(messageDto.getImageUrl());

        // create post
        post = postRepository.save(post);

        return post.getId();
    }

    @Transactional(readOnly = true)
    public PostResponseDto findById (Long id) throws Exception {
        Post post = postRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new NoSuchElementException("해당 게시글이 없습니다. id=" + id));

        return new PostResponseDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostListResponseDto> findAll(){
        return postRepository.findAllDescAndDeletedIsFalse().stream()
                .map(PostListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostListResponseDto> findAllByUser(UserPrincipal userPrincipal){
        // user
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

        return postRepository.findAllByUserEqualsAndDeletedIsFalse(user).stream()
                .map(PostListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostListResponseDto> findAllByMission(Long id){
        // mission
        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mission", "id", id));

        return postRepository.findAllByMissionAndDeletedIsFalse(mission).stream()
                .map(PostListResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 미션별 포스트 제출기록 (전체유저)
     * */
    @Transactional(readOnly = true)
    public PostScheduleResponseDto findSchedule(Long id, String startDate, String endDate){
        // mission
        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mission", "id", id));

        // schedule
        List<PostHistoryDto> historyDtoList = postRepository.findSchedule(id, startDate, endDate);
        Schedule schedule = Schedule.builder()
                                     .historyDtoList(historyDtoList)
                                     .build();

        return  PostScheduleResponseDto.builder()
                                       .users(mission.getAllUser())
                                        .schedules(schedule.getAllSchedule())
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

    @Transactional
    public Long update(Long id, PostUpdateRequestDto requestDto){
        Optional<Post> optional = Optional.ofNullable(postRepository.findById(id))
                .orElseThrow(() -> new NoSuchElementException("해당 게시글이 없습니다. id=" + id));

        Post post = optional.get();
        post.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    @Transactional
    public Long updateImage(Long id, MultipartFile file) throws IOException {
        Optional<Post> optional = Optional.ofNullable(postRepository.findById(id))
                .orElseThrow(() -> new NoSuchElementException("해당 게시글이 없습니다. id=" + id));

        Post post = optional.get();

        // change image
        MessageDto message = imageService.uploadPostS3(file, post.getMission().getTitle() + imageService.genDir());
        post.updateImage(message.getImageUrl());

        return id;
    }


    @Transactional
    public void delete(Long id, UserPrincipal userPrincipal){
        // user
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

        // post
        Optional<Post> optional = Optional.ofNullable(postRepository.findById(id))
                        .orElseThrow(()-> new NoSuchElementException("해당 게시글이 없습니다. id =" + id));

        // delete flag -> 'Y'
        Post post = optional.get();
        post.delete(user);
    }



}
