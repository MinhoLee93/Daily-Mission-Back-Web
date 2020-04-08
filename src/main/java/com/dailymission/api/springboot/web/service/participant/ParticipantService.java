package com.dailymission.api.springboot.web.service.participant;

import com.dailymission.api.springboot.exception.ResourceNotFoundException;
import com.dailymission.api.springboot.security.UserPrincipal;
import com.dailymission.api.springboot.web.dto.participant.ParticipantSaveRequestDto;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.MissionRepository;
import com.dailymission.api.springboot.web.repository.participant.Participant;
import com.dailymission.api.springboot.web.repository.participant.ParticipantRepository;
import com.dailymission.api.springboot.web.repository.user.User;
import com.dailymission.api.springboot.web.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.NotAcceptableStatusException;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ParticipantService {
    // repository
    private final ParticipantRepository participantRepository;
    private final MissionRepository missionRepository;
    private final UserRepository userRepository;
    // password encoder
    private final PasswordEncoder passwordEncoder;

    /**
     * [ 2020-03-11 : 이민호 ]
     * 설명 : 미션에 참여한다.
     * */
    @Transactional
    public boolean save(ParticipantSaveRequestDto requestDto, UserPrincipal userPrincipal) {

        // check mission id
        if(requestDto.getMission()==null){
            throw new IllegalArgumentException("참여할 미션을 선택하지 않았습니다.");
        }

        // check credential
        if(requestDto.getCredential()==null){
            throw new IllegalArgumentException("미션에 참여하기위한 비밀번호가 입력되지 않았습니다.");
        }

        // user
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

        // mission
        Mission mission = missionRepository.findById(requestDto.getMission().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Mission", "id", requestDto.getMission().getId()));


        /**
         * [ 2020-03-11 : 이민호 ]
         * 설명 : 값이 Null 일 수도 있는 경우에는 Optional 을 사용하면 된다.
         * */
        // 이미 참여중인지 확인
        Optional<Participant> optional = participantRepository.findByMissionAndUser(mission,user);
        if(optional.isPresent()){
            // 강퇴된 회원
            if(optional.get().isBanned()){
                throw new IllegalArgumentException("강퇴된 미션에는 참여할 수 없습니다.");
            }else{
                throw new IllegalArgumentException("이미 참여중인 미션입니다.");
            }
        }

        /**
         * [ 2020-03-11 : 이민호 ]
         * 설명 : 참여 가능한 미션인지 확인한다.
         *        1. 종료되지 않은 미션
         *        2. 삭제되지 않은 미션
         *        3. 시작하지 않은 미션
         * */
        if(!mission.isPossibleToParticipate(LocalDate.now())){
            throw new IllegalArgumentException("참여 가능한 미션이 아닙니다.");
        }


        // 비밀번호 확인
        if(!mission.matchCredential(requestDto.getCredential(), passwordEncoder)){
            throw new NotAcceptableStatusException("비밀번호가 일치하지 않습니다.");
        }

        // participant entity
        Participant participant =  requestDto.toEntity(user);

        // save participant
        participantRepository.save(participant).getId();

        return true;
    }

//    @Transactional(readOnly = true)
//    public ParticipantResponseDto findByMissionAndAccount (Mission mission, User user){
//        Optional<Participant> optional = Optional.ofNullable(participantRepository.findByMissionAndUser(mission, user))
//                .orElseThrow(()-> new NoSuchElementException("해당 참여내용은 존재하지 않습니다"));
//
//        Participant participant = optional.get();
//        return new ParticipantResponseDto(participant);
//    }
//
//    @Transactional(readOnly = true)
//    public List<ParticipantListResponseDto> findAllByMission (Mission mission){
//
//        return participantRepository.findAllByMission(mission)
//                .stream()
//                .map(ParticipantListResponseDto::new)
//                .collect(Collectors.toList());
//    }
//
//    @Transactional(readOnly = true)
//    public List<ParticipantListResponseDto> findAllByAccount (User user){
//
//        return participantRepository.findAllByUser(user)
//                .stream()
//                .map(ParticipantListResponseDto::new)
//                .collect(Collectors.toList());
//    }

//    @Transactional
//    public Long ban(ParticipantUpdateRequestDto requestDto){
//        Optional<Participant> optional = Optional.ofNullable(participantRepository.findByMissionAndUser(requestDto.getMission(), requestDto.getUser()))
//                                        .orElseThrow(()-> new NoSuchElementException("미션에 참가하고 있는 사용자가 아닙니다"));
//
//        Participant participant = optional.get();
//        participant.ban();
//
//        return participant.getId();
//    }
}
