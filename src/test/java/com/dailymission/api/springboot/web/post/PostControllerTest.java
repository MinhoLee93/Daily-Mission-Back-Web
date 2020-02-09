package com.dailymission.api.springboot.web.post;

import com.dailymission.api.springboot.web.account.AccountSetup;
import com.dailymission.api.springboot.web.dto.post.PostSaveRequestDto;
import com.dailymission.api.springboot.web.mission.MissionSetup;
import com.dailymission.api.springboot.web.repository.account.Account;
import com.dailymission.api.springboot.web.repository.account.AccountRepository;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.MissionRepository;
import com.dailymission.api.springboot.web.repository.post.Post;
import com.dailymission.api.springboot.web.repository.post.PostRepository;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    private AccountRepository accountRepository;

    private Post post;

    @Before
    public void setup(){
        // 미션 생성자
        AccountSetup accountSetup = AccountSetup.builder().name("미션 생성자").build();
        Account missionCreator = accountSetup.build();
        accountRepository.save(missionCreator);

        // 미션
        MissionSetup missionSetup = MissionSetup.builder().account(missionCreator).build();
        Mission mission = missionSetup.build();
        missionRepository.save(mission);

        // 포스트 작성자
        accountSetup = AccountSetup.builder().name("포스트 생성자").build();
        Account postCreator = accountSetup.build();
        accountRepository.save(postCreator);


        // 포스트
        PostSetup postSetup = PostSetup.builder().account(postCreator).mission(mission).build();
        post = postSetup.build();
    }

    @Test
    public void post_저장후_정상등록_확인() throws Exception {
        // given
        PostSaveRequestDto requestDto = PostSaveRequestDto.builder()
                                                          .mission(post.getMission())
                                                          .account(post.getAccount())
                                                          .title(post.getTitle())
                                                          .content(post.getContent())
                                                          .build();
        // when
        mvc.perform(
                post("/api/post")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new Gson().toJson(requestDto)))
                .andDo(print())
                .andExpect(status().isOk());

        // then
        List<Post> all = postRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo("test");
        assertThat(all.get(0).getContent()).isEqualTo("test");

        System.out.println(new Gson().toJson(all.get(0)));
    }
}
