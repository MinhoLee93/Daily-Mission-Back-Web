package com.dailymission.api.springboot.web.domain.post;


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
    PostRepository postRepository;

    @After
    public void cleanup(){
        postRepository.deleteAll();
    }

    @Test
    public void 게시글_저장후_불러왔을때_동일한값인지(){
            // given
            String title = "테스트 게시글";
            String content = "테스트 본문";

            postRepository.save(Post.builder()
                                .title(title)
                                .content(content)
                                .author("alsgkgk77@gmail.com")
                                .build());

            // when
            List<Post> postList = postRepository.findAll();

            // then
            Post post = postList.get(0);
            assertThat(post.getTitle()).isEqualTo(title);
            assertThat(post.getContent()).isEqualTo(content);
    }
}
