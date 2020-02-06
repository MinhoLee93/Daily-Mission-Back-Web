package com.dailymission.api.springboot.web.dto.post;

import com.dailymission.api.springboot.web.domain.post.Post;
import com.dailymission.api.springboot.web.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class PostSaveRequestDto implements Serializable {
    private String title;
    private String content;
    private User user;

    @Builder
    public PostSaveRequestDto(String title, String content, User user){
        this.title = title;
        this.content = content;
        this.user = User.builder().build();
    }

    public Post toEntitiy(){
        return Post.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
    }
}
