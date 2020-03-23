package com.dailymission.api.springboot.web.post;

import com.dailymission.api.springboot.security.UserPrincipal;
import com.dailymission.api.springboot.web.common.MultipartFileSetup;
import com.dailymission.api.springboot.web.dto.post.PostSaveRequestDto;
import com.dailymission.api.springboot.web.dto.rabbitmq.MessageDto;
import com.dailymission.api.springboot.web.mission.MissionSetup;
import com.dailymission.api.springboot.web.participant.ParticipantSetup;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.MissionRepository;
import com.dailymission.api.springboot.web.repository.participant.Participant;
import com.dailymission.api.springboot.web.repository.participant.ParticipantRepository;
import com.dailymission.api.springboot.web.repository.post.Post;
import com.dailymission.api.springboot.web.repository.post.PostRepository;
import com.dailymission.api.springboot.web.repository.user.User;
import com.dailymission.api.springboot.web.repository.user.UserRepository;
import com.dailymission.api.springboot.web.service.image.ImageService;
import com.dailymission.api.springboot.web.service.post.PostService;
import com.dailymission.api.springboot.web.service.rabbitmq.MessageProducer;
import com.dailymission.api.springboot.web.user.UserSetup;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import java.util.NoSuchElementException;
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
public class PostServiceTest {
    @InjectMocks
    private PostService postService;
    @Mock
    private PostRepository postRepository;
    @Mock
    private MissionRepository missionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ParticipantRepository participantRepository;
    @Mock
    private ImageService imageService;
    @Mock
    private MessageProducer messageProducer;

    private final String TITLE = "TITLE";
    private final String CONTENT = "CONTENT";
    private final String ORIGINAL_FILE_NAME = "ORIGINAL_FILE_NAME.jpg";
    private final String FILE_EXTENSION = ".jpg";
    private final String IMAGE_URL = "IMAGE_URL.jpg";

    private Mission mission;
    private User user;
    private Participant participant;
    private Post post;

    @Before
    public void setup() throws Exception {
        // user
        user = UserSetup.builder().build().get();

        // mission
        mission = MissionSetup.builder().user(user).build().get();

        // participant
        participant = ParticipantSetup.builder().mission(mission).user(user).build().get();

        // post
        post = Post.builder()
                    .user(user)
                    .mission(mission)
                    .title(TITLE)
                    .content(CONTENT)
                    .originalFileName(ORIGINAL_FILE_NAME)
                    .fileExtension(FILE_EXTENSION)
                    .imageUrl(IMAGE_URL)
                    .build();
    }


    /**
     * [ 2020-03-23 : 이민호 ]
     * 설명 : 미션에 참여중인 사용자가 아닌데, 포스트를 제출하는 경우
     *        NoSuchElementException 가 발생한다.
     * */
    @Test(expected = NoSuchElementException.class)
    public void not_participant_user_create_post_THEN_throw_NoSuchElementException() throws Exception {
        // given
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        MultipartFile file = MultipartFileSetup.builder().build().get();
        PostSaveRequestDto requestDto = PostSaveRequestDto.builder()
                                                          .title(TITLE)
                                                          .content(CONTENT)
                                                          .missionId(mission.getId())
                                                          .file(file)
                                                          .build();

        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(missionRepository.findById(any())).willReturn(Optional.of(mission));
        given(participantRepository.findByMissionAndUser(any(), any())).willReturn(Optional.empty());

        // when
        postService.save(requestDto , userPrincipal);
    }

    /**
     * [ 2020-03-23 : 이민호 ]
     * 설명 : 강퇴된 사용자가 포스트를 제출하는 경우
     *        IllegalAccessError 에러가 발생한다.
     * */
    @Test(expected = IllegalAccessError.class)
    public void banned_user_create_post_THEN_throw_IllegalAccessError() throws Exception {
        // given
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        MultipartFile file = MultipartFileSetup.builder().build().get();
        PostSaveRequestDto requestDto = PostSaveRequestDto.builder()
                .title(TITLE)
                .content(CONTENT)
                .missionId(mission.getId())
                .file(file)
                .build();

        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(missionRepository.findById(any())).willReturn(Optional.of(mission));
        Participant spyParticipant = spy(participant);
        given(participantRepository.findByMissionAndUser(any(), any())).willReturn(Optional.of(spyParticipant));
        doReturn(true).when(spyParticipant).isBanned();

        // when
        postService.save(requestDto , userPrincipal);
    }


    /**
     * [ 2020-03-23 : 이민호 ]
     * 설명 : 종료된 미션에 포스트를 제출하는 경우
     *        IllegalArgumentException 에러가 발생한다.
     * */
    @Test(expected = IllegalArgumentException.class)
    public void ended_mission_create_post_THEN_throw_IllegalArgumentException() throws Exception {
        // given
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        MultipartFile file = MultipartFileSetup.builder().build().get();
        PostSaveRequestDto requestDto = PostSaveRequestDto.builder()
                .title(TITLE)
                .content(CONTENT)
                .missionId(mission.getId())
                .file(file)
                .build();

        given(userRepository.findById(any())).willReturn(Optional.of(user));
        Mission spyMission = spy(mission);
        given(missionRepository.findById(any())).willReturn(Optional.of(spyMission));
        doReturn(true).when(spyMission).isEnded();
        Participant spyParticipant = spy(participant);
        given(participantRepository.findByMissionAndUser(any(), any())).willReturn(Optional.of(spyParticipant));
        doReturn(false).when(spyParticipant).isBanned();

        // when
        postService.save(requestDto , userPrincipal);
    }

    /**
     * [ 2020-03-23 : 이민호 ]
     * 설명 : 삭제된 미션에 포스트를 제출하는 경우
     *        IllegalArgumentException 에러가 발생한다.
     * */
    @Test(expected = IllegalArgumentException.class)
    public void deleted_mission_create_post_THEN_throw_IllegalArgumentException() throws Exception {
        // given
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        MultipartFile file = MultipartFileSetup.builder().build().get();
        PostSaveRequestDto requestDto = PostSaveRequestDto.builder()
                .title(TITLE)
                .content(CONTENT)
                .missionId(mission.getId())
                .file(file)
                .build();

        given(userRepository.findById(any())).willReturn(Optional.of(user));
        Mission spyMission = spy(mission);
        given(missionRepository.findById(any())).willReturn(Optional.of(spyMission));
        doReturn(false).when(spyMission).isEnded();
        doReturn(true).when(spyMission).isDeleted();
        Participant spyParticipant = spy(participant);
        given(participantRepository.findByMissionAndUser(any(), any())).willReturn(Optional.of(spyParticipant));
        doReturn(false).when(spyParticipant).isBanned();

        // when
        postService.save(requestDto , userPrincipal);
    }


    /**
     * [ 2020-03-23 : 이민호 ]
     * 설명 : 금일 이미 제출한 미션에대해 포스트를 제출하는 경우
     *        IllegalArgumentException 에러가 발생한다.
     * */
    @Test(expected = IllegalArgumentException.class)
    public void duplicate_create_post_THEN_throw_IllegalArgumentException() throws Exception {
        // given
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        MultipartFile file = MultipartFileSetup.builder().build().get();
        PostSaveRequestDto requestDto = PostSaveRequestDto.builder()
                .title(TITLE)
                .content(CONTENT)
                .missionId(mission.getId())
                .file(file)
                .build();

        given(userRepository.findById(any())).willReturn(Optional.of(user));
        Mission spyMission = spy(mission);
        given(missionRepository.findById(any())).willReturn(Optional.of(spyMission));
        doReturn(false).when(spyMission).isEnded();
        doReturn(false).when(spyMission).isDeleted();
        Participant spyParticipant = spy(participant);
        given(participantRepository.findByMissionAndUser(any(), any())).willReturn(Optional.of(spyParticipant));
        doReturn(false).when(spyParticipant).isBanned();
        given(postRepository.countPostSubmit(any(),any(),any(),any())).willReturn(1L);

        // when
        postService.save(requestDto , userPrincipal);
    }


    /**
     * [ 2020-03-23 : 이민호 ]
     * 설명 : post 제출 성공
     * */
    @Test
    public void post_create_success() throws Exception {
        // given
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        MultipartFile file = MultipartFileSetup.builder().build().get();
        PostSaveRequestDto requestDto = PostSaveRequestDto.builder()
                .title(TITLE)
                .content(CONTENT)
                .missionId(mission.getId())
                .file(file)
                .build();

        given(userRepository.findById(any())).willReturn(Optional.of(user));
        Mission spyMission = spy(mission);
        given(missionRepository.findById(any())).willReturn(Optional.of(spyMission));
        doReturn(false).when(spyMission).isEnded();
        doReturn(false).when(spyMission).isDeleted();
        Participant spyParticipant = spy(participant);
        given(participantRepository.findByMissionAndUser(any(), any())).willReturn(Optional.of(spyParticipant));
        doReturn(false).when(spyParticipant).isBanned();
        given(postRepository.countPostSubmit(any(),any(),any(),any())).willReturn(0L);
        given(imageService.uploadPostS3(any(), any())).willReturn(MessageDto.builder().imageUrl(IMAGE_URL).build());
        given(postRepository.save(any())).willReturn(post);

        // when
        Long id = postService.save(requestDto , userPrincipal);

        // then
        assertThat(id).isEqualTo(post.getId());
    }

    /**
     * [ 2020-03-23 : 이민호 ]
     * 설명 : 삭제할 수 없는 POST 인 경우 삭제되지 않는다.
     * */
    @Test
    public void is_deletable_false_THEN_post_not_deleted() throws Exception {
        // given
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        Long id = post.getId();

        given(userRepository.findById(any())).willReturn(Optional.of(user));
        Post spyPost = spy(post);
        given(postRepository.findByIdAndDeletedIsFalse(any())).willReturn(Optional.of(spyPost));
        doReturn(false).when(spyPost).isDeletable(any());

        // when
        postService.delete(id, userPrincipal);

        // then
        assertThat(spyPost.isDeleted()).isFalse();
    }


    /**
     * [ 2020-03-23 : 이민호 ]
     * 설명 : 삭제할 수 있는 POST 인 경우 삭제된다.
     * */
    @Test
    public void is_deletable_true_THEN_post_deleted() throws Exception {
        // given
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        Long id = post.getId();

        given(userRepository.findById(any())).willReturn(Optional.of(user));
        Post spyPost = spy(post);
        given(postRepository.findByIdAndDeletedIsFalse(any())).willReturn(Optional.of(spyPost));
        doReturn(true).when(spyPost).isDeletable(any());

        // when
        postService.delete(id, userPrincipal);

        // then
        assertThat(spyPost.isDeleted()).isTrue();
    }
}
