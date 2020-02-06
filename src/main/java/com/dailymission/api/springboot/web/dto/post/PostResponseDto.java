package com.dailymission.api.springboot.web.dto.post;

import com.dailymission.api.springboot.web.domain.post.Post;
import com.dailymission.api.springboot.web.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class PostResponseDto implements Serializable {
    private  Long id;
    private String title;
    private String content;
    private User user;

    public PostResponseDto(Post entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.user = entity.getUser();
    }
}
