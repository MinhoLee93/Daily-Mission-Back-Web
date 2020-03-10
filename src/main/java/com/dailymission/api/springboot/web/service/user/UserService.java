package com.dailymission.api.springboot.web.service.user;

import com.dailymission.api.springboot.exception.ResourceNotFoundException;
import com.dailymission.api.springboot.security.CurrentUser;
import com.dailymission.api.springboot.security.UserPrincipal;
import com.dailymission.api.springboot.web.dto.mission.MissionMockDto;
import com.dailymission.api.springboot.web.dto.user.UserResponseDto;
import com.dailymission.api.springboot.web.dto.user.UserUpdateRequestDto;
import com.dailymission.api.springboot.web.repository.participant.Participant;
import com.dailymission.api.springboot.web.repository.user.User;
import com.dailymission.api.springboot.web.repository.user.UserRepository;
import com.dailymission.api.springboot.web.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserService {

    private final PostService postService;
    private final UserRepository userRepository;

    public UserResponseDto getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {

        // user
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

        // response
        UserResponseDto userResponseDto = UserResponseDto.builder()
                                                        .id(user.getId())
                                                        .name(user.getName())
                                                        .email(user.getEmail())
                                                        .imageUrl(user.getImageUrl())
                                                        .build();

        // submit (당일 + 익익 새벽 3시까지 각 미션별 제출했는지?)
        for(Participant participant : user.getParticipants()){

            // deleted 미션 제외
            if(participant.getMission().isDeleted()){
                continue;
            }

            LocalDateTime start = LocalDateTime.now();
            boolean isSubmit = false;

            // 0시  ~ 03시
            if(start.isBefore(LocalDate.now().atTime(3, 0))){
                // 전날 새벽 3시 ~ 현재
                isSubmit = postService.findSubmitHistory(participant.getMission().getId(),
                        user.getId(),
                        LocalDate.now().atTime(3,0).minusDays(1),
                        start);
            }else{
            // 03시 ~ 24시
                // 전날 새벽 3시 ~ 현재
                isSubmit = postService.findSubmitHistory(participant.getMission().getId(),
                        user.getId(),
                        LocalDate.now().atTime(3,0),
                        start);
            }

            MissionMockDto missionMockDto = MissionMockDto.builder()
                                                        .entity(participant.getMission())
                                                        .banned(participant.isBanned())
                                                        .submit(isSubmit)
                                                        .build();

            // add mission with today submit history
            userResponseDto.addMissionMock(missionMockDto);
        }

        return userResponseDto;
    }

    public Long updateUser(UserUpdateRequestDto requestDto, @CurrentUser UserPrincipal userPrincipal){

        // check current user
        if(requestDto.getId()!=userPrincipal.getId()){
            throw new IllegalAccessError("본인의 유저 정보만 변경할 수 있습니다.");
        }

        // user
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));



        return  1L;
    }
}
