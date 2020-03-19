package com.dailymission.api.springboot.web.user;

import com.dailymission.api.springboot.security.UserPrincipal;
import com.dailymission.api.springboot.web.dto.rabbitmq.MessageDto;
import com.dailymission.api.springboot.web.dto.user.UserResponseDto;
import com.dailymission.api.springboot.web.dto.user.UserUpdateRequestDto;
import com.dailymission.api.springboot.web.repository.user.AuthProvider;
import com.dailymission.api.springboot.web.repository.user.User;
import com.dailymission.api.springboot.web.repository.user.UserRepository;
import com.dailymission.api.springboot.web.service.image.ImageService;
import com.dailymission.api.springboot.web.service.post.PostService;
import com.dailymission.api.springboot.web.service.rabbitmq.MessageProducer;
import com.dailymission.api.springboot.web.service.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

/**
 * [ 2020-03-18 : 이민호 ]
 * - 테스트 진행시 중요 관점이 아닌 것들은 Mocking 처리해서 외부 의존성들을 줄일 수 있습니다.
 * - 예를 들어 주문 할인 로직이 제대로 동작하는지에 대한 테스트만 진행하지 이게 실제로 데이터베이스에 insert 되는지는 해당 테스트의 관심사가 아닙니다.
 * - 주로 Service 영역을 테스트 합니다.
 * */
@RunWith(MockitoJUnitRunner.class)
//@ActiveProfiles(TestProfile.TEST)
//@Ignore
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private ImageService imageService;
    @Mock
    private PostService postService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MessageProducer messageProducer;

    // user
    private User user;

    private final String USER_NAME = "USER_NAME";
    private final String EMAIL = "EMAIL@gmail.com";
    private final String IMAGE_URL = "IMAGE_URL.jpg";
    private final AuthProvider PROVIDER = AuthProvider.google;
    private final String PROVIDER_ID = "PROVIDER_ID";



    @Before
    public void setup() throws Exception {
        // save user
        user = User.builder()
                    .name(USER_NAME)
                    .email(EMAIL)
                    .imageUrl(IMAGE_URL)
                    .provider(PROVIDER)
                    .providerId(PROVIDER_ID)
                    .build();
    }


    /**
     * [ 2020-03-18 : 이민호 ]
     * 설명 : @CurrentUser UserPrincipal 정보를 return 한다.
     * */
    @Test
    public void call_get_current_user_THEN_return_current_user(){
        // given
        final UserPrincipal userPrincipal = UserPrincipal.create(user);
        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(postService.isSubmitToday(any())).willReturn(false);

        // when
        final UserResponseDto responseDto = userService.getCurrentUser(userPrincipal);

        // then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getMissions().size()).isEqualTo(user.getParticipants().size());
        assertThat(responseDto.getEmail()).isEqualTo(user.getEmail());
        assertThat(responseDto.getName()).isEqualTo(user.getName());
    }


    /**
     * [ 2020-03-18 : 이민호 ]
     * 설명 : USER UPDATE 시 User id 가 NULL 이면
     *        IllegalArgumentException 에러가 발생한다.
     * */
    @Test(expected = IllegalArgumentException.class)
    public void update_user_with_userId_null_throw_IllegalArgumentException() throws IOException {
        // given
        final UserUpdateRequestDto requestDto = UserUpdateRequestDto.builder().id(null).build();
        final UserPrincipal userPrincipal = UserPrincipal.create(user);

        // when
        userService.updateUser(requestDto, userPrincipal);
    }


    /**
     * [ 2020-03-18 : 이민호 ]
     * 설명 : USER UPDATE 시 UserPrincipal 의 id와 UserUpdateRequestDto 의 id가 다르면
     *       IllegalArgumentException 에러가 발생한다.
     * */
    @Test(expected = IllegalAccessError.class)
    public void update_user_with_UserPrincipal_id_isNotEqualTo_requestDto_id_throw_IllegalArgumentException() throws IOException {
        // given
        final UserUpdateRequestDto requestDto = UserUpdateRequestDto.builder().id(2L).build();
        user.setId(1L);
        final UserPrincipal userPrincipal = UserPrincipal.create(user);

        // when
        userService.updateUser(requestDto, userPrincipal);
    }



    /**
     * [ 2020-03-18 : 이민호 ]
     * 설명 : USER UPDATE 시 name 과 file 이 둘다 NULL 이면
     *        IllegalArgumentException 에러가 발생한다.
     * */
    @Test(expected = IllegalArgumentException.class)
    public void update_user_with_file_and_name_both_null_throw_IllegalArgumentException() throws IOException {
        // given
        user.setId(1L);
        final UserUpdateRequestDto requestDto = UserUpdateRequestDto.builder().id(user.getId()).build();
        final UserPrincipal userPrincipal = UserPrincipal.create(user);

        // when
        userService.updateUser(requestDto, userPrincipal);
    }


    /**
     * [ 2020-03-18 : 이민호 ]
     * 설명 : USER Update 가 성공한다.
     * */
    @Test
    public void update_user_success() throws IOException {
        // given
        MultipartFile file = new MockMultipartFile("file",
                                     "NameOfTheFile",
                                        "multipart/form-data",
                                                     new FileInputStream("C:/portfolio/daily-mission/src/test/resources/로고.jpg"));

        user.setId(1L);
        final String UPDATE_USER_NAME = "UPDATE_USER_NAME";
        final String UPDATE_IMAGE_URL = "UPDATE_IMAGE_URL";
        final String DIR_NAME = "DIR_NAME";
        final String ORIGINAL_FILE_NAME = "ORIGINAL_FILE_NAME";
        final String FILE_EXTENSION = ".jpg";

        final UserUpdateRequestDto requestDto = UserUpdateRequestDto.builder()
                                                                    .id(user.getId())
                                                                    .file(file)
                                                                    .userName(UPDATE_USER_NAME)
                                                                    .build();

        final UserPrincipal userPrincipal = UserPrincipal.create(user);
        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(imageService.getUserDir(any())).willReturn(DIR_NAME);
        given(imageService.uploadUserS3(any(),any())).willReturn(MessageDto.builder()
                                                                            .imageUrl(UPDATE_IMAGE_URL)
                                                                            .originalFileName(ORIGINAL_FILE_NAME)
                                                                            .extension(FILE_EXTENSION)
                                                                            .build());

        doNothing().when(messageProducer).sendMessage(any(User.class), any(MessageDto.class));
        given(userRepository.save(any(User.class))).willReturn(user);

        // when
        Long updateUserId = userService.updateUser(requestDto, userPrincipal);

        // then
        assertThat(updateUserId).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo(UPDATE_USER_NAME);
        assertThat(user.getImageUrl()).isEqualTo(UPDATE_IMAGE_URL);
    }
}
