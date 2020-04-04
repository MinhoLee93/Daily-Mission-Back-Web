package com.dailymission.api.springboot.web.post;

import com.dailymission.api.springboot.security.UserPrincipal;
import com.dailymission.api.springboot.web.common.MultipartFileSetup;
import com.dailymission.api.springboot.web.dto.post.PostSaveRequestDto;
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
import com.dailymission.api.springboot.web.user.UserSetup;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * [ 2020-03-23 : 이민호 ]
 * - 모든 Bean 을 올리고 테스트를 진행하기 때문에 쉽게 테스트 진행 가능합니다.
 * - API 를 테스트할 경우 요청부터 응답까지 전체적인 테스트 진행 가능합니다.
 * - @Transactional 트랜잭션 어노테이션을 추가하면 테스트코드의 데이터베이스 정보가 자동으로 Rollback 됩니다.
 * - 요청에 대한 메서드를 requestSignUp(...)으로 분리해서 재사용성을 높입니다.
 * - 해당 메서드로 validate 를 실패하는 케이스도 작성합니다.
 * - andDo(print()) 메서드를 추가해서 해당 요청에 대한 출력을 확인합니다. 디버깅에 매우 유용합니다.
 * - 모든 response 에 대한 andExpect 를 작성합니다.
 * - response 에 하나라도 빠지거나 변경되면 API 변경이 이루어진 것이고 그 변경에 맞게 테스트 코드도 변경되어야 합니다.
 * - 회원 조회 테스트 강은 경우 memberSetup.save(); 메서드로 테스트전에 데이터베이스에 insert 합니다.
 * - 데이터베이스에 미리 있는 값을 검증하는 것은 데이터베이스 상태에 의존한 코드가 되며 누군가가 회원 정보를 변경하게 되면 테스트 코드가 실패하게 됩니다.
 * - 테스트 전에 데이터를 insert 하지 않는다면 테스트 코드 구동 전에 .sql 으로 미리 데이터베이스를 준비시킵니다.
 * - 중요한 것은 데이터베이스 상태에 너무 의존적인 테스트는 향후 로직의 문제가 없더라도 테스트가 실패하는 상황이 자주 만나게 됩니다.
 * */
@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.config.location=" +
        "classpath:/application.yml" +
        ",classpath:/application-oauth.yml" +
        ",classpath:/aws.yml"
)
@Transactional
public class PostControllerTest {
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private PostRepository postRepository;

    private Mission mission;
    private Participant participant;
    private Post post;
    private User user;
    private MockMvc mvc;

    private final String TITLE = "TITLE";
    private final String CONTENT = "CONTENT";
    private final String ORIGINAL_FILE_NAME = "ORIGINAL_FILE_NAME.jpg";
    private final String FILE_EXTENSION = ".jpg";
    private final String IMAGE_URL = "IMAGE_URL.jpg";

    @Before
    public void setup(){
        // mvc
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        // save user
        user = userRepository.save(UserSetup.builder().build().get());

        // save mission
        mission = missionRepository.save(MissionSetup.builder().user(user).build().get());

        // save participant
        participant = participantRepository.save(ParticipantSetup.builder().mission(mission).user(user).build().get());

    }

    /**
     * [ 2020-03-23 : 이민호 ]
     * 설명 : 포스트 제출에 성공한다.
     * */
    @Test
    public void post_save_success() throws Exception {
        // given
        final UserPrincipal userPrincipal = UserPrincipal.create(user);
        final MultipartFile file = MultipartFileSetup.builder().build().get();
        final PostSaveRequestDto requestDto = PostSaveRequestDto.builder()
                                                                .missionId(mission.getId())
                                                                .title(TITLE)
                                                                .content(CONTENT)
                                                                .file(file)
                                                                .build();

        // when
        final ResultActions resultActions = postRequest(requestDto, userPrincipal);
        MvcResult result =  resultActions
                                    .andExpect(status().isOk())
                                    .andReturn();
        String savePostId = result.getResponse().getContentAsString();

        // then
        assertThat(savePostId).isNotNull();
    }


    /**
     * [ 2020-03-23 : 이민호 ]
     * 설명 : 포스트의 Detail 정보를 가져온다.
     *       { "postId":85,
     *         "missionId":98,
     *         "missionTitle":"TITLE",
     *         "userName":"USER_NAME",
     *         "userThumbnailUrl":"THUMBNAIL_URL.jpg",
     *         "title":"TITLE",
     *         "content":"CONTENT",
     *         "thumbnailUrl":"IMAGE_URL.jpg",
     *         "createdDate":"2020-03-23T16:08:36.589",
     *         "modifiedDate":"2020-03-23T16:08:36.589"
     *       }
     * */
    @Test
    public void post_find_by_id_success() throws Exception {
        // given
        post = postRepository.save(PostSetup.builder().mission(mission).user(user).build().get());
        UserPrincipal userPrincipal = UserPrincipal.create(user);

        // when
        final ResultActions resultActions = getRequest("/api/post/" + post.getId(), userPrincipal);


        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("postId").value(post.getId()))
                .andExpect(jsonPath("missionId").value(mission.getId()))
                .andExpect(jsonPath("missionTitle").value(mission.getTitle()))
                .andExpect(jsonPath("userName").value(user.getName()))
                .andExpect(jsonPath("userThumbnailUrl").value(user.getThumbnailUrl()))
                .andExpect(jsonPath("title").value(post.getTitle()))
                .andExpect(jsonPath("content").value(post.getContent()))
                .andExpect(jsonPath("thumbnailUrl").value(post.getThumbnailUrl()));
    }


    /**
     * [ 2020-03-23 : 이민호 ]
     * 설명 : 포스트를 삭제한다.
     *        {
     *         "id":69
     *        }
     * */
    @Test
    public void post_delete_success() throws Exception {
        // given
        post = postRepository.save(PostSetup.builder().mission(mission).user(user).build().get());
        final UserPrincipal userPrincipal = UserPrincipal.create(user);
        Long id = post.getId();

        // when
        final ResultActions resultActions = deleteRequest("/api/post/delete/" + id, userPrincipal);

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(post.getId()));
    }


    /**
     * [ 2020-03-23 : 이민호 ]
     * 설명 : 전체 포스트 목록을 가져온다.
     *      [
     *        {"postId":92,
     *         "missionId":105,
     *         "missionTitle":"TITLE",
     *         "userName":"USER_NAME",
     *         "userThumbnailUrl":"THUMBNAIL_URL.jpg",
     *         "title":"TITLE",
     *         "content":"CONTENT",
     *         "imageUrl":"IMAGE_URL.jpg",
     *         "thumbnailUrl":"IMAGE_URL.jpg",
     *         "modifiedDate":"2020-03-23T16:27:53.664"
     *        }, ..
     *      ]
     * */
    @Test
    public void post_findAll_success() throws Exception {
        // given
        post = postRepository.save(PostSetup.builder().mission(mission).user(user).build().get());
        final UserPrincipal userPrincipal = UserPrincipal.create(user);

        // when
        final ResultActions resultActions = getRequest("/api/post/all", userPrincipal);

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0]postId").value(post.getId()))
                .andExpect(jsonPath("[0]missionId").value(mission.getId()))
                .andExpect(jsonPath("[0]missionTitle").value(mission.getTitle()))
                .andExpect(jsonPath("[0]userName").value(user.getName()))
                .andExpect(jsonPath("[0]userThumbnailUrl").value(user.getThumbnailUrl()))
                .andExpect(jsonPath("[0]title").value(post.getTitle()))
                .andExpect(jsonPath("[0]content").value(post.getContent()))
                .andExpect(jsonPath("[0]imageUrl").value(post.getImageUrl()))
                .andExpect(jsonPath("[0]thumbnailUrl").value(post.getThumbnailUrl()));
    }


    /**
     * [ 2020-03-23 : 이민호 ]
     * 설명 : 유저별 포스트 목록을 가져온다.
     *      [
     *        {"postId":92,
     *         "missionId":105,
     *         "missionTitle":"TITLE",
     *         "userName":"USER_NAME",
     *         "userThumbnailUrl":"THUMBNAIL_URL.jpg",
     *         "title":"TITLE",
     *         "content":"CONTENT",
     *         "imageUrl":"IMAGE_URL.jpg",
     *         "thumbnailUrl":"IMAGE_URL.jpg",
     *         "modifiedDate":"2020-03-23T16:27:53.664"
     *        }, ..
     *      ]
     * */
    @Test
    public void post_findAll_me_success() throws Exception {
        // given
        post = postRepository.save(PostSetup.builder().mission(mission).user(user).build().get());
        final UserPrincipal userPrincipal = UserPrincipal.create(user);

        // when
        final ResultActions resultActions = getRequest("/api/post/all/me", userPrincipal);

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0]postId").value(post.getId()))
                .andExpect(jsonPath("[0]missionId").value(mission.getId()))
                .andExpect(jsonPath("[0]missionTitle").value(mission.getTitle()))
                .andExpect(jsonPath("[0]userName").value(user.getName()))
                .andExpect(jsonPath("[0]userThumbnailUrl").value(user.getThumbnailUrl()))
                .andExpect(jsonPath("[0]title").value(post.getTitle()))
                .andExpect(jsonPath("[0]content").value(post.getContent()))
                .andExpect(jsonPath("[0]imageUrl").value(post.getImageUrl()))
                .andExpect(jsonPath("[0]thumbnailUrl").value(post.getThumbnailUrl()));
    }


    /**
     * [ 2020-03-23 : 이민호 ]
     * 설명 : 미션별 포스트 목록을 가져온다.
     *      [
     *        {"postId":92,
     *         "missionId":105,
     *         "missionTitle":"TITLE",
     *         "userName":"USER_NAME",
     *         "userThumbnailUrl":"THUMBNAIL_URL.jpg",
     *         "title":"TITLE",
     *         "content":"CONTENT",
     *         "imageUrl":"IMAGE_URL.jpg",
     *         "thumbnailUrl":"IMAGE_URL.jpg",
     *         "modifiedDate":"2020-03-23T16:27:53.664"
     *        }, ..
     *      ]
     * */
    @Test
    public void post_findAll_by_mission_success() throws Exception {
        // given
        post = postRepository.save(PostSetup.builder().mission(mission).user(user).build().get());
        final UserPrincipal userPrincipal = UserPrincipal.create(user);
        Long missionId = mission.getId();

        // when
        final ResultActions resultActions = getRequest("/api/post/all/mission/" + missionId, userPrincipal);

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0]postId").value(post.getId()))
                .andExpect(jsonPath("[0]missionId").value(mission.getId()))
                .andExpect(jsonPath("[0]missionTitle").value(mission.getTitle()))
                .andExpect(jsonPath("[0]userName").value(user.getName()))
                .andExpect(jsonPath("[0]userThumbnailUrl").value(user.getThumbnailUrl()))
                .andExpect(jsonPath("[0]title").value(post.getTitle()))
                .andExpect(jsonPath("[0]content").value(post.getContent()))
                .andExpect(jsonPath("[0]imageUrl").value(post.getImageUrl()))
                .andExpect(jsonPath("[0]thumbnailUrl").value(post.getThumbnailUrl()));
    }



    /**
     * [ 2020-03-23 : 이민호 ]
     * 설명 : 미션별로 해당 Week 의 post 제출 schedule 을 가져온다.
     *        week : 0 -> 이번주
     *        week : n -> n 주전
     *
     *        일/월/화/수/목/금/토 정보를 가져온다.
     *
     *      { "weekDates":
     *       [
     *          {"date":"2020-03-22","day":"SUNDAY","mandatory":true},
     *          {"date":"2020-03-23","day":"MONDAY","mandatory":true},
     *          {"date":"2020-03-24","day":"TUESDAY","mandatory":true},
     *          {"date":"2020-03-25","day":"WEDNESDAY","mandatory":true},
     *          {"date":"2020-03-26","day":"THURSDAY","mandatory":true},
     *          {"date":"2020-03-27","day":"FRIDAY","mandatory":false},
     *          {"date":"2020-03-28","day":"SATURDAY","mandatory":false}
     *        ],
     *      "histories":[..??] 여긴 왜 loading 이 안되는걸까?
     *      }
     * */
    @Test
    public void post_find_schedule_success() throws Exception {
        // given
        post = postRepository.save(PostSetup.builder().mission(mission).user(user).build().get());
        final UserPrincipal userPrincipal = UserPrincipal.create(user);
        Long missionId = mission.getId();

        // when
        final ResultActions resultActions = getRequest("/api/post/schedule/mission/" + missionId + "/week/0", userPrincipal);

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("weekDates[0].day").value("SUNDAY"))
                .andExpect(jsonPath("weekDates[1].day").value("MONDAY"))
                .andExpect(jsonPath("weekDates[2].day").value("TUESDAY"))
                .andExpect(jsonPath("weekDates[3].day").value("WEDNESDAY"))
                .andExpect(jsonPath("weekDates[4].day").value("THURSDAY"))
                .andExpect(jsonPath("weekDates[5].day").value("FRIDAY"))
                .andExpect(jsonPath("weekDates[6].day").value("SATURDAY"));
    }



    /**
     * [ 2020-03-22 : 이민호 ]
     * 설명 : getRequest 를 수행한다.
     *       UserDetails 를 with(user(***)) 에 넘겨, Authenticated User 를 사용한다.
     *       api 에 호출할 GET Rest API 를 입력한다. ex) api = "/api/mission/51"
     * */
    private ResultActions getRequest(String api, UserPrincipal userPrincipal) throws Exception {
        return  mvc.perform(
                get(api)
                        .with(user(userPrincipal)))
                .andDo(print());
    }

    /**
     * [ 2020-03-22 : 이민호 ]
     * 설명 : deleteRequest 를 수행한다.
     *        UserDetails 를 with(user(***)) 에 넘겨, Authenticated User 를 사용한다.
     *        api 에 호출할 DELETE Rest API 를 입력한다. ex) api = "/api/post/delete/51"
     * */
    private ResultActions deleteRequest(String api, UserPrincipal userPrincipal) throws Exception {
        return  mvc.perform(
                delete(api)
                        .with(user(userPrincipal)))
                .andDo(print());
    }

    /**
     * [ 2020-03-19 : 이민호 ]
     * 설명 : postRequest 를 수행한다.
     *       UserDetails 를 with(user(***)) 에 넘겨, Authenticated User 를 사용한다.
     *       MockMultipartFile 을 생성해 multipart 의 file 에 넘긴다.
     *       multipart/form-data 는 requestBody JSON 이 아닌 param 으로 변수를 받는다.
     * */
    private ResultActions postRequest(PostSaveRequestDto requestDto, UserPrincipal userPrincipal) throws Exception {
        final MockMultipartFile file = MultipartFileSetup.builder().build().get();

        return  mvc.perform(
                multipart("/api/post")
                        .file(file)
                        .param("missionId", requestDto.getMissionId().toString())
                        .param("title",requestDto.getTitle())
                        .param("content", requestDto.getContent())
                        .with(user(userPrincipal)))
                .andDo(print());
    }
}
