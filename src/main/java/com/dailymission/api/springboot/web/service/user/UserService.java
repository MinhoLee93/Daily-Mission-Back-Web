package com.dailymission.api.springboot.web.service.user;

import com.dailymission.api.springboot.exception.ResourceNotFoundException;
import com.dailymission.api.springboot.security.CurrentUser;
import com.dailymission.api.springboot.security.UserPrincipal;
import com.dailymission.api.springboot.web.dto.mission.MissionMockDto;
import com.dailymission.api.springboot.web.dto.rabbitmq.MessageDto;
import com.dailymission.api.springboot.web.dto.user.UserResponseDto;
import com.dailymission.api.springboot.web.dto.user.UserUpdateRequestDto;
import com.dailymission.api.springboot.web.repository.participant.Participant;
import com.dailymission.api.springboot.web.repository.user.User;
import com.dailymission.api.springboot.web.repository.user.UserRepository;
import com.dailymission.api.springboot.web.service.image.ImageService;
import com.dailymission.api.springboot.web.service.post.PostService;
import com.dailymission.api.springboot.web.service.rabbitmq.MessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserService {
    private final ImageService imageService;
    private final PostService postService;
    private final UserRepository userRepository;
    private final MessageProducer messageProducer;

    @Transactional(readOnly = true)
    public UserResponseDto getCurrentUser(@CurrentUser UserPrincipal userPrincipal){

        // user
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

        // response
        UserResponseDto userResponseDto = UserResponseDto.builder()
                                                        .id(user.getId())
                                                        .name(user.getName())
                                                        .email(user.getEmail())
                                                        .thumbnailUrl(user.getThumbnailUrl())
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

    @Transactional
    public Long updateUser(UserUpdateRequestDto requestDto, @CurrentUser UserPrincipal userPrincipal) throws IOException {

        // check current user
        if(requestDto.getId()!=userPrincipal.getId()){
            throw new IllegalAccessError("본인의 유저 정보만 변경할 수 있습니다.");
        }

        // user
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));


        // change name
        if(requestDto.getUserName()!=null){
            user.setName(requestDto.getUserName());
        }

        // change image
        if(requestDto.getFile()!=null){
            String dirName = "" + user.getProvider().toString() + "/" + user.getId();
            MessageDto message = imageService.uploadUserS3(requestDto.getFile(), dirName);
            user.updateImage(message.getImageUrl());

            /**
             * 리팩토링 필요
             * */
            String originalFileName = requestDto.getFile().getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            user.setOriginalFileName(originalFileName);
            user.setFileExtension(fileExtension);

            // produce message
            messageProducer.sendMessage(user, message);
        }

        userRepository.save(user);

        return  1L;
    }
}
