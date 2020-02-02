package com.dailymission.api.springboot.web.domain.posts;


import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @After
    public void cleanup(){
        postsRepository.deleteAll();
    }

    @Test
    public void 게시글_저장후_불러왔을때_동일한값인지(){
            // given
            String title = "테스트 게시글";
            String content = "테스트 본문";

            postsRepository.save(Posts.builder()
                                .title(title)
                                .content(content)
                                .author("alsgkgk77@gmail.com")
                                .build());

            // when
            List<Posts> postsList = postsRepository.findAll();

            // then
            Posts posts = postsList.get(0);
            assertThat(posts.getTitle()).isEqualTo(title);
            assertThat(posts.getContent()).isEqualTo(content);
    }
}
