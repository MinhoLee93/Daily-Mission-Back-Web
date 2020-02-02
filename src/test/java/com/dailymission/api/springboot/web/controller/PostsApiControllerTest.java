package com.dailymission.api.springboot.web.controller;

import com.dailymission.api.springboot.web.domain.posts.Posts;
import com.dailymission.api.springboot.web.domain.posts.PostsRepository;
import com.dailymission.api.springboot.web.dto.PostsSaveRequestDto;
import com.dailymission.api.springboot.web.dto.PostsUpdateRequestDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {

    // field
    private Posts posts;
    private PostsSaveRequestDto requestDto;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @Before
    public void setup(){
        posts = Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build();

       requestDto =  PostsSaveRequestDto.builder()
                .title(posts.getTitle())
                .content(posts.getContent())
                .author(posts.getAuthor())
                .build();
    }

    @After
    public void tearDown() throws Exception{
        postsRepository.deleteAll();
    }

    @Test
    public void Posts_저장후_조회했을때_동일한값인지() throws Exception {
        // given
        String url = "http://localhost:" + port + "/api/posts";

        // when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(requestDto.getTitle());
        assertThat(all.get(0).getContent()).isEqualTo(requestDto.getContent());
    }

    @Test
    public void Posts_수정후_조회했을때_수정된값인지() throws Exception {

        // given
        postsRepository.save(posts);

        Long updateId = posts.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                                            .title(expectedTitle)
                                            .content(expectedContent)
                                            .build();

        String url = "http://localhost:" + port + "/api/posts/" + updateId;

        HttpEntity<PostsUpdateRequestDto> requestEntity  = new HttpEntity<>(requestDto);

        // when
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity , Long.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);

    }

    @Test
    public void Posts_BaseTimeEntity_저장후_시간확인() throws Exception {
        // given
        LocalDateTime now = LocalDateTime.of(2020,2,2,0,0,0);
        postsRepository.save(posts);

        // when
        List<Posts> postsList = postsRepository.findAll();

        // then
        System.out.println(">>>>>>>> createDate=" + posts.getCreatedDate() + ", modifiedDate=" + posts.getModifiedDate());

        assertThat(posts.getCreatedDate()).isGreaterThan(now);
        assertThat(posts.getModifiedDate()).isGreaterThan(now);
    }
}
