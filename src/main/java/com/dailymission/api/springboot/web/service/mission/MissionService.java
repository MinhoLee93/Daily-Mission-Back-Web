package com.dailymission.api.springboot.web.service.mission;

import com.dailymission.api.springboot.exception.ResourceNotFoundException;
import com.dailymission.api.springboot.security.UserPrincipal;
import com.dailymission.api.springboot.web.dto.mission.*;
import com.dailymission.api.springboot.web.dto.rabbitmq.MessageDto;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.MissionRepository;
import com.dailymission.api.springboot.web.repository.participant.Participant;
import com.dailymission.api.springboot.web.repository.participant.ParticipantRepository;
import com.dailymission.api.springboot.web.repository.user.User;
import com.dailymission.api.springboot.web.repository.user.UserRepository;
import com.dailymission.api.springboot.web.service.image.ImageService;
import com.dailymission.api.springboot.web.service.rabbitmq.MessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MissionService {
    // service
    private final ImageService imageService;
    // repository
    private final MissionRepository missionRepository;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    // message producer
    private final MessageProducer messageProducer;
    // password encoder
    private final PasswordEncoder passwordEncoder;

    /**
     * [ 2020-03-12 : 이민호 ]
     * 설명 : 신규 미션을 생성한다.
     * */
    @Transactional
    public String save(MissionSaveRequestDto requestDto, UserPrincipal userPrincipal) throws Exception {

        // file
        if(requestDto.getFile()==null){
            throw new IllegalArgumentException("미션 생성시 사진은 필수 입니다.");
        }

        // user entity
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

        // mission entity
        Mission mission = requestDto.toEntity(user);


        if(!mission.isValidMission(LocalDate.now())){
            throw new IllegalArgumentException("MISSION 데이터를 확인해 주세요");
        }

        // set credential
        String credential = mission.setCredential(passwordEncoder);

        // upload image
        MessageDto message = imageService.uploadMissionS3(requestDto.getFile(), mission.getTitle());
        mission.updateImage(message.getImageUrl());

        // create mission
        mission = missionRepository.save(mission);

        // produce message
        message.setUserId(user.getId());
        message.setMissionId(mission.getId());
        messageProducer.sendMessage(mission , message);

        /**
         * [ 2020-03-12 : 이민호 ]
         * 설명 : 미션 생성자 (방장) 은 해당 미션에 바로 참여
         * */
        Participant participant = Participant.builder()
                                            .mission(mission)
                                            .user(user)
                                            .build();

        // save participant
        participantRepository.save(participant);

        /**
         * [ 2020-03-12 : 이민호 ]
         * 설명 : 임의로 생성한 credential 을 return 한다.
         * */
        return credential;
    }


    /**
     * [ 2020-03-12 : 이민호 ]
     * 설명 : 미션의 디테일 정보와 참여중인 사용자들의 이름/사진/강퇴여부를 가져온다
     * */
    @Transactional(readOnly = true)
    public MissionResponseDto findById (Long id){

        // find mission by id
        Mission mission = missionRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mission.", "id", id));


        // return mission response
        return new MissionResponseDto(mission);
    }


    /**
     * [ 2020-03-12 : 이민호 ]
     * 설명 : Hot 미션 목록을 가져온다.
     * */
    @Transactional(readOnly = true)
    public List<MissionHotListResponseDto> findHotList(){
        return missionRepository.findAllByParticipantSize().stream()
                .map(MissionHotListResponseDto::new)
                .collect(Collectors.toList());
    }


    /**
     * [ 2020-03-12 : 이민호 ]
     * 설명 : New 미션 목록을 가져온다.
     * */
    @Transactional(readOnly = true)
    public List<MissionNewListResponseDto> findNewList(){
        return missionRepository.findAllCreatedInMonth().stream()
                .map(MissionNewListResponseDto::new)
                .collect(Collectors.toList());
    }


    /**
     * [ 2020-03-12 : 이민호 ]
     * 설명 : 전체 미션 목록을 가져온다.
     * */
    @Transactional(readOnly = true)
    public List<MissionAllListResponseDto> findAllList(){
        return missionRepository.findAlByCreatedDate().stream()
                .map(MissionAllListResponseDto::new)
                .collect(Collectors.toList());
    }



//    @Transactional
//    public Long update(Long id, MissionUpdateRequestDto requestDto){
//        Optional<Mission> optional = Optional.ofNullable(missionRepository.findByIdAndDeletedIsFalse(id))
//                            .orElseThrow(() -> new NoSuchElementException("해당 미션이 없습니다. id=" + id));
//
//        Mission mission = optional.get();
//        mission.update(requestDto.getMissionRule(),
//                       requestDto.getTitle(),
//                       requestDto.getContent(),
//                       requestDto.getStartDate(),
//                       requestDto.getEndDate());
//
//        return id;
//    }



//    @Transactional
//    public Long updateImage(Long id, MultipartFile file) throws IOException {
//        Optional<Mission> optional = Optional.ofNullable(missionRepository.findByIdAndDeletedIsFalse(id))
//                .orElseThrow(() -> new NoSuchElementException("해당 미션이 없습니다. id=" + id));
//
//        Mission mission = optional.get();
//
//        // change image
//        MessageDto message = imageService.uploadMissionS3(file, mission.getTitle());
//        mission.updateImage(message.getImageUrl());
//
//        return id;
//    }

    /**
     * [ 2020-03-12 : 이민호 ]
     * 설명 : 미션을 삭제한다.
     *        삭제 요청은 방장만 할 수 있다.
     *        미션 시작전 & 참여한 사용자가 없을 경우에만 삭제 가능하다.
     * */
    @Transactional
    public void delete(Long id, UserPrincipal userPrincipal){

        // user
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

        /**
         * [ 2020-03-12 : 이민호 ]
         * 설명 : Optional 은 Null 일 수도 있는 경우에 사용해야한다.
         *        현재의 경우 Mission 이 없으면 안되므로 사용하지 말자.
         * */
        // Optional<Mission> optional = Optional.ofNullable(missionRepository.findByIdAndDeletedIsFalse(id))
        //                           .orElseThrow(() -> new NoSuchElementException("해당 미션이 없습니다. id=" + id));

        // mission
        Mission mission = missionRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mission.", "id", id));


        // check is deletable
        if(mission.isDeletable(user)){

            // delete mission
            mission.delete();
        }
    }


    /**
     * [ 2020-03-12 : 이민호 ]
     * 설명 : 매일 새벽 3시에 미션 종료 batch 가 수행된다.
     *        endDate 가 지난 미션을 종료한다.
     *        모두 강퇴되서 참여자가 0명인 미션을 종료한다.
     * */
    @Transactional
    public void end(Long id){

        // mission entity
        Mission mission = missionRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mission", "id", id));


        // check is end able
        if(mission.isEndable(LocalDate.now())){
            // mission end
            mission.end();
        }
    }
}
