package com.dailymission.api.springboot.web.participant;

import com.dailymission.api.springboot.security.UserPrincipal;
import com.dailymission.api.springboot.web.dto.participant.ParticipantSaveRequestDto;
import com.dailymission.api.springboot.web.mission.MissionSetup;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.MissionRepository;
import com.dailymission.api.springboot.web.repository.participant.Participant;
import com.dailymission.api.springboot.web.repository.participant.ParticipantRepository;
import com.dailymission.api.springboot.web.repository.user.User;
import com.dailymission.api.springboot.web.repository.user.UserRepository;
import com.dailymission.api.springboot.web.service.participant.ParticipantService;
import com.dailymission.api.springboot.web.user.UserSetup;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

/**
 * [ 2020-03-18 : 이민호 ]
 * - 테스트 진행시 중요 관점이 아닌 것들은 Mocking 처리해서 외부 의존성들을 줄일 수 있습니다.
 * - 예를 들어 주문 할인 로직이 제대로 동작하는지에 대한 테스트만 진행하지 이게 실제로 데이터베이스에 insert 되는지는 해당 테스트의 관심사가 아닙니다.
 * - 주로 Service 영역을 테스트 합니다.
 * */
@RunWith(MockitoJUnitRunner.class)
public class ParticipantServiceTest {
    @InjectMocks
    private ParticipantService participantService;
    @Mock
    private MissionRepository missionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ParticipantRepository participantRepository;

    private Mission mission;
    private User user;
    private Participant participant;

    @Before
    public void setup() throws Exception {
        // user
        user = UserSetup.builder().build().get();

        // mission
        mission = MissionSetup.builder().user(user).build().get();

        // participant
        participant = Participant.builder().user(user).mission(mission).build();
    }


    /**
     * [ 2020-03-22: 이민호 ]
     * 설명 : 참여할 미션이 NULL 일 경우
     *        IllegalArgumentException 가 발생한다.
     * */
    @Test(expected = IllegalArgumentException.class)
    public void save_with_mission_id_isNull_THEN_throw_IllegalArgumentException() {
        // given
        ParticipantSaveRequestDto requestDto = ParticipantSaveRequestDto.builder()
                                                                        .credential(mission.getCredential())
                                                                        .build();

        UserPrincipal userPrincipal = UserPrincipal.create(user);

        // when
        participantService.save(requestDto, userPrincipal);
    }

    /**
     * [ 2020-03-22: 이민호 ]
     * 설명 : credential 값이 NULL 일 경우
     *        IllegalArgumentException 가 발생한다.
     * */
    @Test(expected = IllegalArgumentException.class)
    public void save_with_credential_isNull_THEN_throw_IllegalArgumentException() {
        // given
        ParticipantSaveRequestDto requestDto = ParticipantSaveRequestDto.builder()
                .mission(mission)
                .build();

        UserPrincipal userPrincipal = UserPrincipal.create(user);

        // when
        participantService.save(requestDto, userPrincipal);
    }

    /**
     * [ 2020-03-22: 이민호 ]
     * 설명 : 이미 참여중인 MISSION 이거나, 강퇴당한 경우
     *        IllegalArgumentException 가 발생한다.
     * */
    @Test(expected = IllegalArgumentException.class)
    public void already_joined_same_mission_THEN_throw_IllegalArgumentException() {
        // given
        ParticipantSaveRequestDto requestDto = ParticipantSaveRequestDto.builder()
                                                                        .mission(mission)
                                                                        .credential(mission.getCredential())
                                                                        .build();
        UserPrincipal userPrincipal = UserPrincipal.create(user);

        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(missionRepository.findById(any())).willReturn(Optional.of(mission));
        /**
         * [ 2020-03-22: 이민호 ]
         * 설명 : Cannot mock/spy class java.util.Optional
         * */
        Optional<Participant> optional = Optional.of(participant);
        given(participantRepository.findByMissionAndUser(any(), any())).willReturn(optional);

        // when
        participantService.save(requestDto, userPrincipal);
    }

    /**
     * [ 2020-03-22: 이민호 ]
     * 설명 : 참여가능하지 않은 미션(삭제,종료) 인 경우
     *        IllegalArgumentException 가 발생한다.
     * */
    @Test(expected = IllegalArgumentException.class)
    public void possible_to_participant_is_false_THEN_throw_IllegalArgumentException() {
        // given
        ParticipantSaveRequestDto requestDto = ParticipantSaveRequestDto.builder()
                                                                        .mission(mission)
                                                                        .credential(mission.getCredential())
                                                                        .build();
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        Mission spyMission = spy(mission);
        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(missionRepository.findById(any())).willReturn(Optional.of(spyMission));
        /**
         * [ 2020-03-22: 이민호 ]
         * 설명 : Optional.empty() = NULL
         * */
        given(participantRepository.findByMissionAndUser(any(), any())).willReturn(Optional.empty());

        doReturn(false).when(spyMission).isPossibleToParticipate(any());

        // when
        participantService.save(requestDto, userPrincipal);
    }


    /**
     * [ 2020-03-22: 이민호 ]
     * 설명 : 비밀번호가 틀렸을 경우
     *        IllegalArgumentException 가 발생한다.
     * */
    @Test(expected = IllegalArgumentException.class)
    public void credential_is_match_THEN_throw_IllegalArgumentException() {
        // given
        ParticipantSaveRequestDto requestDto = ParticipantSaveRequestDto.builder()
                                                                        .mission(mission)
                                                                        .credential(mission.getCredential())
                                                                        .build();
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        Mission spyMission = spy(mission);
        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(missionRepository.findById(any())).willReturn(Optional.of(spyMission));
        /**
         * [ 2020-03-22: 이민호 ]
         * 설명 : Optional.empty() = NULL
         * */
        given(participantRepository.findByMissionAndUser(any(), any())).willReturn(Optional.empty());

        doReturn(true).when(spyMission).isPossibleToParticipate(any());
        doReturn(false).when(spyMission).matchCredential(any(),any());

        // when
        participantService.save(requestDto, userPrincipal);
    }


    /**
     * [ 2020-03-22: 이민호 ]
     * 설명 : Participant save 성공
     * */
    @Test
    public void participant_save_success() {
        // given
        ParticipantSaveRequestDto requestDto = ParticipantSaveRequestDto.builder()
                                                                        .mission(mission)
                                                                        .credential(mission.getCredential())
                                                                        .build();
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        Mission spyMission = spy(mission);
        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(missionRepository.findById(any())).willReturn(Optional.of(spyMission));
        /**
         * [ 2020-03-22: 이민호 ]
         * 설명 : Optional.empty() = NULL
         * */
        given(participantRepository.findByMissionAndUser(any(), any())).willReturn(Optional.empty());
        given(participantRepository.save(any())).willReturn(participant);
        doReturn(true).when(spyMission).isPossibleToParticipate(any());
        doReturn(true).when(spyMission).matchCredential(any(),any());

        // when
        Long id = participantService.save(requestDto, userPrincipal);

        // then
        assertThat(id).isEqualTo(participant.getId());
    }
}
