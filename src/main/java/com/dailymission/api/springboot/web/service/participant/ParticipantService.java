package com.dailymission.api.springboot.web.service.participant;

import com.dailymission.api.springboot.web.dto.participant.ParticipantListResponseDto;
import com.dailymission.api.springboot.web.dto.participant.ParticipantResponseDto;
import com.dailymission.api.springboot.web.dto.participant.ParticipantSaveRequestDto;
import com.dailymission.api.springboot.web.dto.participant.ParticipantUpdateRequestDto;
import com.dailymission.api.springboot.web.repository.account.Account;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.MissionRepository;
import com.dailymission.api.springboot.web.repository.participant.Participant;
import com.dailymission.api.springboot.web.repository.participant.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    private final MissionRepository missionRepository;

    @Transactional
    public Long save(ParticipantSaveRequestDto requestDto){
        Optional<Mission> optional = missionRepository.findById(requestDto.getMission().getId());
        Mission mission = optional.get();

        // 비밀번호 확인
        if(!mission.checkCredential(requestDto.getCredential())){
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        return participantRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional(readOnly = true)
    public ParticipantResponseDto findByMissionAndAccount (Mission mission, Account account){
        Optional<Participant> optional = Optional.ofNullable(participantRepository.findByMissionAndAccount(mission, account))
                .orElseThrow(()-> new NoSuchElementException("해당 참여내용은 존재하지 않습니다"));

        Participant participant = optional.get();
        return new ParticipantResponseDto(participant);
    }

    @Transactional(readOnly = true)
    public List<ParticipantListResponseDto> findAllByMission (Mission mission){

        return participantRepository.findAllByMission(mission)
                .stream()
                .map(ParticipantListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ParticipantListResponseDto> findAllByAccount (Account account){

        return participantRepository.findAllByAccount(account)
                .stream()
                .map(ParticipantListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long ban(ParticipantUpdateRequestDto requestDto){
        Optional<Participant> optional = Optional.ofNullable(participantRepository.findByMissionAndAccount(requestDto.getMission(), requestDto.getAccount()))
                                        .orElseThrow(()-> new NoSuchElementException("미션에 참가하고 있는 사용자가 아닙니다"));

        Participant participant = optional.get();
        participant.ban();

        return participant.getId();
    }
}
