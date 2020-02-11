package com.dailymission.api.springboot.web.post;

import com.dailymission.api.springboot.web.dto.post.PostSaveRequestDto;
import com.dailymission.api.springboot.web.dto.post.PostUpdateRequestDto;
import com.dailymission.api.springboot.web.repository.user.User;
import com.dailymission.api.springboot.web.repository.user.UserRepository;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.MissionRepository;
import com.dailymission.api.springboot.web.repository.post.Post;
import com.dailymission.api.springboot.web.repository.post.PostRepository;
import com.dailymission.api.springboot.web.service.post.PostService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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

    private Post post;

    @Before
    public void setup() throws Exception {
        Mission mission = Mission.builder().build();
        User user = User.builder().build();
        post = Post.builder()
                    .mission(mission)
                    .user(user)
                    .build();
    }

    @Test
    public void post_저장_성공() throws IOException {
        // given
        PostSaveRequestDto requestDto = PostSaveRequestDto.builder()
                                                          .mission(post.getMission())
                                                          .user(post.getUser())
                                                          .title(post.getTitle())
                                                          .content(post.getContent())
                                                          .build();

        given(missionRepository.existsById(any())).willReturn(true);
        given(userRepository.existsById(any())).willReturn(true);
        given(postRepository.save(any())).willReturn(post);

        // when
        Long id  = postService.save(requestDto, null);

        // then
        assertThat(id).isEqualTo(post.getId());
    }

    @Test(expected = NoSuchElementException.class)
    public void post_조회시_조회된_결과가_없을경우_에러가발생하는지(){
        // given
        given(postRepository.findById(any())).willReturn(null);

        // when
        postService.findById(1L);
    }

    @Test(expected = NoSuchElementException.class)
    public void post_업데이트시_업데이트할_post가_없을경우_에러가발생하는지(){
        // given
        given(postRepository.findById(any())).willReturn(null);
        Long id = 1L;
        PostUpdateRequestDto requestDto = PostUpdateRequestDto.builder().build();

        // when
        postService.update(1L, requestDto);
    }

    @Test
    public void post_업데이트시_업데이트할_post_있을경우_정상적으로_업데이트_하는지(){
        // given
        Post post = Post.builder().build();
        Long id = 1L;
        PostUpdateRequestDto requestDto = PostUpdateRequestDto.builder()
                .title("update")
                .content("update")
                .build();

        given(postRepository.findById(any())).willReturn(java.util.Optional.ofNullable(post));

        // when
        postService.update(1L, requestDto);

        // then
        assertThat(post.getTitle()).isEqualTo("update");
        assertThat(post.getContent()).isEqualTo("update");
    }
}
