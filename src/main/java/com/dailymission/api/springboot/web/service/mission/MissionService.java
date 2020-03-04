package com.dailymission.api.springboot.web.service.mission;

import com.dailymission.api.springboot.exception.ResourceNotFoundException;
import com.dailymission.api.springboot.security.UserPrincipal;
import com.dailymission.api.springboot.web.dto.mission.*;
import com.dailymission.api.springboot.web.dto.rabbitmq.MessageDto;
import com.dailymission.api.springboot.web.repository.common.Validator;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.MissionRepository;
import com.dailymission.api.springboot.web.repository.participant.Participant;
import com.dailymission.api.springboot.web.repository.participant.ParticipantRepository;
import com.dailymission.api.springboot.web.repository.user.User;
import com.dailymission.api.springboot.web.repository.user.UserRepository;
import com.dailymission.api.springboot.web.service.image.ImageService;
import com.dailymission.api.springboot.web.service.rabbitmq.MessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MissionService {

    private final MissionRepository missionRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final ParticipantRepository participantRepository;
    private final MessageProducer messageProducer;

    @Transactional
    public String save(MissionSaveRequestDto requestDto, UserPrincipal userPrincipal) throws Exception {
        // data validation
        Validator.builder().build().checkValidation(requestDto);

        // user
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

        // entity
        Mission mission = requestDto.toEntity(user);

        // upload image
        MessageDto message = imageService.uploadMissionS3(requestDto.getFile(), mission.getTitle());
        mission.updateImage(message.getImageUrl());

        // create mission
        mission = missionRepository.save(mission);

        // produce message
        messageProducer.sendMessage(mission , message);

        // create participant (미션 생성자는 바로 참여)
        Participant participant = Participant.builder()
                                            .mission(mission)
                                            .user(user)
                                            .build();

        // save participant
        participantRepository.save(participant);

        return mission.getCredential();
    }

    @Transactional(readOnly = true)
    public MissionResponseDto findById (Long id){
        Optional<Mission> optional = Optional.ofNullable(missionRepository.findById(id))
                .orElseThrow(() -> new NoSuchElementException("해당 미션이 없습니다. id=" + id));

        Mission mission = optional.get();
        return new MissionResponseDto(mission);
    }

    @Transactional(readOnly = true)
    public List<MissionHomeListResponseDto> findHomeListByCreatedDate(){
        return missionRepository.findAllByCreatedDate().stream()
                .map(MissionHomeListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MissionAllListResponseDto> findAllListByCreatedDate(){
        return missionRepository.findAllByCreatedDate().stream()
                .map(MissionAllListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MissionHotListResponseDto> findHotListByCreatedDate(){
        return missionRepository.findAllByCreatedDate().stream()
                .map(MissionHotListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long update(Long id, MissionUpdateRequestDto requestDto){
        Optional<Mission> optional = Optional.ofNullable(missionRepository.findById(id))
                            .orElseThrow(() -> new NoSuchElementException("해당 미션이 없습니다. id=" + id));

        Mission mission = optional.get();
        mission.update(requestDto.getMissionRule(),
                       requestDto.getTitle(),
                       requestDto.getContent(),
                       requestDto.getStartDate(),
                       requestDto.getEndDate());

        return id;
    }

    @Transactional
    public Long updateImage(Long id, MultipartFile file) throws IOException {
        Optional<Mission> optional = Optional.ofNullable(missionRepository.findById(id))
                .orElseThrow(() -> new NoSuchElementException("해당 미션이 없습니다. id=" + id));

        Mission mission = optional.get();

        // change image
        MessageDto message = imageService.uploadMissionS3(file, mission.getTitle());
        mission.updateImage(message.getImageUrl());

        return id;
    }


    @Transactional
    public void delete(Long id, UserPrincipal userPrincipal){
        // user
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

        // mission
        Optional<Mission> optional = Optional.ofNullable(missionRepository.findById(id))
                .orElseThrow(() -> new NoSuchElementException("해당 룰이 없습니다. id=" + id));


        // delete flag -> 'Y'
        Mission mission = optional.get();
        mission.delete(user);
    }

    @Transactional
    public void end(Long id){
        Optional<Mission> optional = Optional.ofNullable(missionRepository.findById(id))
                .orElseThrow(() -> new NoSuchElementException("해당 룰이 없습니다. id=" + id));

        // end flag -> 'Y'
        Mission mission = optional.get();
        mission.end();
    }
}
