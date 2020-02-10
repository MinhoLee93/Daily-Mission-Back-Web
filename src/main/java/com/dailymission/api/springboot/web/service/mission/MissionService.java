package com.dailymission.api.springboot.web.service.mission;

import com.dailymission.api.springboot.web.dto.mission.MissionListResponseDto;
import com.dailymission.api.springboot.web.dto.mission.MissionResponseDto;
import com.dailymission.api.springboot.web.dto.mission.MissionSaveRequestDto;
import com.dailymission.api.springboot.web.dto.mission.MissionUpdateRequestDto;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.MissionRepository;
import com.dailymission.api.springboot.web.service.image.ImageService;
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

    private final ImageService imageService;

    @Transactional
    public Long save(MissionSaveRequestDto requestDto, MultipartFile file) throws Exception {
        // create mission with default image
        Mission mission = missionRepository.save(requestDto.toEntity());

        // change image
        if(!file.isEmpty()){
            String imagePath = imageService.uploadS3(file, mission.getTitle());
            mission.updateImage(imagePath);
        }

        return mission.getId();

    }

    @Transactional(readOnly = true)
    public MissionResponseDto findById (Long id){
        Optional<Mission> optional = Optional.ofNullable(missionRepository.findById(id))
                .orElseThrow(() -> new NoSuchElementException("해당 미션이 없습니다. id=" + id));

        Mission mission = optional.get();
        return new MissionResponseDto(mission);
    }

    @Transactional(readOnly = true)
    public List<MissionListResponseDto> findAllDesc(){
        return missionRepository.findAllDesc().stream()
                .map(MissionListResponseDto::new)
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
        String imagePath = imageService.uploadS3(file, mission.getTitle());
        mission.updateImage(imagePath);

        return id;
    }


    @Transactional
    public void delete(Long id){
        Optional<Mission> optional = Optional.ofNullable(missionRepository.findById(id))
                .orElseThrow(() -> new NoSuchElementException("해당 룰이 없습니다. id=" + id));

        // delete flag -> 'Y'
        Mission mission = optional.get();
        mission.delete();
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
