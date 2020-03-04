package com.dailymission.api.springboot.web.service.user;

import com.dailymission.api.springboot.exception.ResourceNotFoundException;
import com.dailymission.api.springboot.security.CurrentUser;
import com.dailymission.api.springboot.security.UserPrincipal;
import com.dailymission.api.springboot.web.dto.mission.MissionMockDto;
import com.dailymission.api.springboot.web.dto.user.UserResponseDto;
import com.dailymission.api.springboot.web.repository.participant.Participant;
import com.dailymission.api.springboot.web.repository.user.User;
import com.dailymission.api.springboot.web.repository.user.UserRepository;
import com.dailymission.api.springboot.web.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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
            // 금일 03시 ~ 내일 03시
            boolean isSubmit = postService.findSubmitHistory(participant.getMission().getId(),
                                                             user.getId(),
                                                             LocalDate.now().atTime(3,0),
                                                             LocalDate.now().atTime(3,0).plusDays(1));

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

}
