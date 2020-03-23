package com.dailymission.api.springboot.web.service.user;

import com.dailymission.api.springboot.exception.ResourceNotFoundException;
import com.dailymission.api.springboot.security.CurrentUser;
import com.dailymission.api.springboot.security.UserPrincipal;
import com.dailymission.api.springboot.web.dto.mission.MissionPostSubmitCheckDto;
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
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserService {
    // service
    private final ImageService imageService;
    private final PostService postService;

    // repository
    private final UserRepository userRepository;

    // message producer
    private final MessageProducer messageProducer;

    /**
     * [ 2020-03-11 : 이민호 ]
     * 설명 : 본인의 유저 정보를 가져온다.
     *        아이디, 이름, 이메일, 썸네일, 참여중인 미션 목록 및 당일 포스트 제출여부
     * */
    @Transactional(readOnly = true)
    public UserResponseDto getCurrentUser(@CurrentUser UserPrincipal userPrincipal){

        /**
         * [ 2020-03-11 : 이민호 ]
         * 설명 : @CurrentUser UserPrincipal 정보를 가져올때 UserRepository.findById 와 같은 조회 query 를 수행한다.
         * */
        // user entity
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

        // user response dto
        UserResponseDto userResponseDto = UserResponseDto.builder()
                                                        .id(user.getId())
                                                        .name(user.getName())
                                                        .email(user.getEmail())
                                                        .thumbnailUrl(user.getThumbnailUrl())
                                                        .build();

        /**
         * [ 2020-03-11 : 이민호 ]
         * 설명 : participant 조회시 user 정보와 mission 정보를 left outer join 해서 같이 가져온다.
         *
         * select *
         * from participant
         * left outer join mission      on      mission.id
         * left outer join user         on      user.id
         * */
        // check post today submit for every attended missions
        for(Participant participant : user.getParticipants()){

            // skip for deleted mission
            if(participant.getMission().isDeleted()){
                continue;
            }

            // check today submit history
            LocalDateTime now = LocalDateTime.now();
            boolean isSubmit = postService.isSubmitToday(participant, now);

            /**
             * [ 2020-03-11 : 이민호 ]
             * 설명 : 참여중인 mission 정보 + 당일 포스트 제출여부 & 강퇴여부
             * */
            MissionPostSubmitCheckDto missionPostSubmitCheckDto = MissionPostSubmitCheckDto.builder()
                                                        .entity(participant.getMission())
                                                        .banned(participant.isBanned())
                                                        .submit(isSubmit)
                                                        .build();

            /**
             * [ 2020-03-11 : 이민호 ]
             * 설명 : user 정보에 참여중인 mission 정보와 당일 포스트 제출여부를 추가한다.
             * */
            // add to userResponseDto
            userResponseDto.addMissionPostSubmitCheckDto(missionPostSubmitCheckDto);

        }

        // return response dto
        return userResponseDto;
    }


    /**
     * [ 2020-03-11 : 이민호 ]
     * 설명 : 유저의 정보를 업데이트 한다. (이름, 이미지)
     * */
    @Transactional
    public Long updateUser(UserUpdateRequestDto requestDto, @CurrentUser UserPrincipal userPrincipal) throws IOException {

        // check use id
        if(requestDto.getId()==null){
            throw new IllegalArgumentException("변경할 유저의 아이디가 입력되지 않았습니다.");
        }

        // check current user
        if(requestDto.getId()!=userPrincipal.getId()){
            throw new IllegalAccessError("본인의 유저 정보만 변경할 수 있습니다.");
        }

        // check request data is exist
        if(requestDto.getFile()==null && requestDto.getUserName()==null){
            throw new IllegalArgumentException("변경할 정보가 존재하지 않습니다.");
        }

        /**
         * [ 2020-03-11 : 이민호 ]
         * 설명 : UserPrincipal <-> User 중복된다.
         *       하지만, UserPrincipal 은 security 를 위해서만 관리하는것이 좋을것 같아..
         *       중복부븐을 합치지는 않겠다.
         * */
        // user entity
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));


        // change user name
        if(user.isValidUpdateName(requestDto.getUserName())){
            user.updateName(requestDto.getUserName());
        }

        /**
         * [ 2020-03-11 : 이민호 ]
         * 설명 : 변경한 이미지를 AWS 에 저장하고, Message 를 생성해 리사이징을 요청한다. (rabbitmq)
         * */
        // change user image
        if(requestDto.getFile()!=null){
            // get dir name
            String dirName = imageService.getUserDir(user);

            // upload image
            MessageDto message = imageService.uploadUserS3(requestDto.getFile(), dirName);

            // update user image & thumbnail url (DB)
            user.updateImage(message.getImageUrl());

            // update origin file name & file extension (DB)
            user.setOriginalFileName(message.getOriginalFileName());
            user.setFileExtension(message.getExtension());

            // produce message for image resize
            messageProducer.sendMessage(user, message);
        }

        // update user info
        userRepository.save(user);

        // return user id
        return  user.getId();
    }
}
