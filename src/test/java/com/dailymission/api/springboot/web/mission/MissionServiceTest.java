package com.dailymission.api.springboot.web.mission;

import com.dailymission.api.springboot.security.UserPrincipal;
import com.dailymission.api.springboot.web.common.MultipartFileSetup;
import com.dailymission.api.springboot.web.dto.mission.MissionSaveRequestDto;
import com.dailymission.api.springboot.web.dto.rabbitmq.MessageDto;
import com.dailymission.api.springboot.web.mission.rule.MissionRuleSetup;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.MissionRepository;
import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import com.dailymission.api.springboot.web.repository.participant.ParticipantRepository;
import com.dailymission.api.springboot.web.repository.user.User;
import com.dailymission.api.springboot.web.repository.user.UserRepository;
import com.dailymission.api.springboot.web.service.image.ImageService;
import com.dailymission.api.springboot.web.service.mission.MissionService;
import com.dailymission.api.springboot.web.service.rabbitmq.MessageProducer;
import com.dailymission.api.springboot.web.user.UserSetup;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
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
public class MissionServiceTest {

    @InjectMocks
    private MissionService missionService;
    @Mock
    private ImageService imageService;
    @Mock
    private MissionRepository missionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ParticipantRepository participantRepository;
    @Mock
    private MissionRule missionRule;
    @Mock
    private User user;
    @Mock
    private MessageProducer messageProducer;

    // mission
    private Mission mission;
    private String credential;

    private final String TITLE = "TITLE";
    private final String CONTENT = "MISSION_CONTENT";
    private final String ORIGINAL_FILE_NAME = "ORIGINAL_FILE_NAME.jpg";
    private final String FILE_EXTENSION = ".jpg";
    private final LocalDate START_DATE = LocalDate.of(2020,04,01);
    private final LocalDate END_DATE = LocalDate.of(2020,04,28);
    private final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    private final String IMAGE_URL = "IMAGE_URL.jpg";


    @Before
    public void setup() throws Exception {
        // save user
        user = UserSetup.builder().build().get();

        // mission rule (fri/sat :false)
        missionRule = MissionRuleSetup.builder().build().get();

        // mission
        mission = Mission.builder()
                .user(user)
                .missionRule(missionRule)
                .title(TITLE)
                .content(CONTENT)
                .originalFileName(ORIGINAL_FILE_NAME)
                .fileExtension(FILE_EXTENSION)
                .startDate(START_DATE)
                .endDate(END_DATE)
                .build();

        // credential
        credential = mission.setCredential(PASSWORD_ENCODER);
    }

    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : MISSION isEndable 가 FALSE 일 경우 END 되지 않는다.
     * */
    @Test
    public void mission_endable_false_THEN_mission_is_not_ended() throws IOException {

        /**
         * [ 2020-03-20 : 이민호 ]
         * 설명 : spy 로 mission 의 isEndable 을 Mocking 한다.
         * */
        Mission spyMission = spy(mission);
        doReturn(false).when(spyMission).isEndable(any());

        // given
        given(missionRepository.findByIdAndDeletedIsFalse(any())).willReturn(Optional.of(spyMission));

        // when
        missionService.end(spyMission.getId());

        // then
        assertThat(spyMission.isEnded()).isFalse();
    }

    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : MISSION isEndable 가 TRUE 일 경우 END 된다.
     * */
    @Test
    public void mission_endable_true_THEN_mission_is_ended() throws IOException {

        /**
         * [ 2020-03-20 : 이민호 ]
         * 설명 : spy 로 mission 의 isEndable 을 Mocking 한다.
         * */
        Mission spyMission = spy(mission);
        doReturn(true).when(spyMission).isEndable(any());

        // given
        given(missionRepository.findByIdAndDeletedIsFalse(any())).willReturn(Optional.of(spyMission));

        // when
        missionService.end(spyMission.getId());

        // then
        assertThat(spyMission.isEnded()).isTrue();
    }

    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : MISSION isDeletable 가 FALSE 일 경우 DELETE 되지 않는다.
     * */
    @Test
    public void mission_deletable_false_THEN_mission_is_not_deleted() throws IOException {
        /**
         * [ 2020-03-20 : 이민호 ]
         * 설명 : spy 로 mission 의 isDeletable 을 Mocking 한다.
         * */
        Mission spyMission = spy(mission);
        doReturn(false).when(spyMission).isDeletable(any());

        // given
        Long id = mission.getId();
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        given(missionRepository.findByIdAndDeletedIsFalse(any())).willReturn(Optional.of(spyMission));
        given(userRepository.findById(any())).willReturn(Optional.of(user));

        // when
        missionService.delete(id, userPrincipal);

        // then
        assertThat(spyMission.isDeleted()).isFalse();
    }

    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : MISSION isDeletable 가 TRUE 일 경우 DELETE 된다.
     * */
    @Test
    public void mission_deletable_true_THEN_mission_is_deleted() throws IOException {
        /**
         * [ 2020-03-20 : 이민호 ]
         * 설명 : spy 로 mission 의 isDeletable 을 Mocking 한다.
         * */
        Mission spyMission = spy(mission);
        doReturn(true).when(spyMission).isDeletable(any());

        // given
        Long id = mission.getId();
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        given(missionRepository.findByIdAndDeletedIsFalse(any())).willReturn(Optional.of(spyMission));
        given(userRepository.findById(any())).willReturn(Optional.of(user));

        // when
        missionService.delete(id, userPrincipal);

        // then
        assertThat(spyMission.isDeleted()).isTrue();
        /**
         * [ 2020-03-20 : 이민호 ]
         * 설명 : deleted mission 은 자동으로 end 된다.
         * */
        assertThat(spyMission.isEnded()).isTrue();
    }

    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : MISSION 생성시 FIle 이 Null 이면
     *        IllegalArgumentException 에러가 발생한다.
     * */
    @Test(expected = IllegalArgumentException.class)
    public void mission_create_with_file_null_THEN_throw_IllegalArgumentException() throws Exception {
        /**
         * [ 2020-03-20 : 이민호 ]
         * 설명 : spy 로 MissionSaveRequestDto 의 getFile 을 Mocking 한다.
         * */
        MissionSaveRequestDto requestDto = spy(MissionSaveRequestDto.builder().build());
        doReturn(null).when(requestDto).getFile();

        // given
        UserPrincipal userPrincipal = UserPrincipal.create(user);

        // when
        missionService.save(requestDto, userPrincipal);
    }

    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : MISSION 생성시 isValidMission 이 False 면
     *       IllegalArgumentException 에러가 발생한다.
     * */
    @Test(expected = IllegalArgumentException.class)
    public void mission_create_isValidMission_is_false_THEN_throw_IllegalArgumentException() throws Exception {

        /**
         * [ 2020-03-20 : 이민호 ]
         * 설명 : spy 로 MissionSaveRequestDto 의 getFile 을 Mocking 한다.
         * */
        MissionSaveRequestDto spyRequestDto = spy(MissionSaveRequestDto.builder().build());
        MultipartFile file = MultipartFileSetup.builder().build().get();
        doReturn(file).when(spyRequestDto).getFile();

        /**
         * [ 2020-03-20 : 이민호 ]
         * 설명 : spy 로 Mission 의 isValidMission 을 Mocking 한다.
         * */
        Mission spyMission = spy(mission);
        doReturn(false).when(spyMission).isValidMission(any());
        doReturn(spyMission).when(spyRequestDto).toEntity(any());

        // given
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        given(userRepository.findById(any())).willReturn(Optional.of(user));

        // when
        missionService.save(spyRequestDto, userPrincipal);
    }

    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : MISSION 이 정상 생생되며, 임의로 생성된 credential 값을 return 받는다.
     * */
    @Test
    public void mission_create_success() throws Exception {
        /**
         * [ 2020-03-20 : 이민호 ]
         * 설명 : spy 로 MissionSaveRequestDto 의 getFile 을 Mocking 한다.
         * */
        MissionSaveRequestDto spyRequestDto = spy(MissionSaveRequestDto.builder().build());
        MultipartFile file = MultipartFileSetup.builder().build().get();
        doReturn(file).when(spyRequestDto).getFile();

        /**
         * [ 2020-03-20 : 이민호 ]
         * 설명 : spy 로 Mission 의 isValidMission 을 Mocking 한다.
         * */
        Mission spyMission = spy(mission);
        doReturn(true).when(spyMission).isValidMission(any());
        doReturn(spyMission).when(spyRequestDto).toEntity(any());
        doReturn("credential").when(spyMission).setCredential(any());

        // given
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(imageService.uploadMissionS3(any(), any())).willReturn(MessageDto.builder()
                                                                               .imageUrl(IMAGE_URL)
                                                                               .build());
        given(missionRepository.save(any())).willReturn(mission);

        // when
        String credential = missionService.save(spyRequestDto, userPrincipal);

        // then
        assertThat(credential).isNotNull();
    }
}
