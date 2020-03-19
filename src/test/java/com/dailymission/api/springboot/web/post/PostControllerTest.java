package com.dailymission.api.springboot.web.post;

import com.dailymission.api.springboot.web.repository.mission.MissionRepository;
import com.dailymission.api.springboot.web.repository.post.Post;
import com.dailymission.api.springboot.web.repository.post.PostRepository;
import com.dailymission.api.springboot.web.repository.user.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PostControllerTest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private UserRepository userRepository;

    private Post post;

    @Before
    public void setup(){
//        // 미션 생성자
//        UserSetup userSetup = UserSetup.builder().name("미션 생성자").build();
//        User missionCreator = userSetup.build();
//        userRepository.save(missionCreator);
//
//        // 미션
//        MissionSetup missionSetup = MissionSetup.builder().user(missionCreator).build();
//        Mission mission = missionSetup.build();
//        missionRepository.save(mission);
//
//        // 포스트 작성자
//        userSetup = UserSetup.builder().name("포스트 생성자").build();
//        User postCreator = userSetup.build();
//        userRepository.save(postCreator);
//
//
//        // 포스트
//        PostSetup postSetup = PostSetup.builder().user(postCreator).mission(mission).build();
//        post = postSetup.build();
    }

    @Test
    public void post_저장후_정상등록_확인() throws Exception {
//        // given
//        PostSaveRequestDto requestDto = PostSaveRequestDto.builder()
//                                                          .mission(post.getMission())
//                                                          .user(post.getUser())
//                                                          .title(post.getTitle())
//                                                          .content(post.getContent())
//                                                          .build();
//        // when
//        mvc.perform(
//                post("/api/post")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(new Gson().toJson(requestDto)))
//                .andDo(print())
//                .andExpect(status().isOk());
//
//        // then
//        List<Post> all = postRepository.findAll();
//        assertThat(all.get(0).getTitle()).isEqualTo("test");
//        assertThat(all.get(0).getContent()).isEqualTo("test");
//
//        System.out.println(new Gson().toJson(all.get(0)));
    }
}
