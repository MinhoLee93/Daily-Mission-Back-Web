package com.dailymission.api.springboot.web.dto.post;

import com.dailymission.api.springboot.web.domain.post.Post;
import com.dailymission.api.springboot.web.domain.user.User;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PostListResponseDto implements Serializable {
    private Long id;
    private String title;
    private User user;
    private LocalDateTime modifiedDate;

    public PostListResponseDto(Post entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.user = entity.getUser();
        this.modifiedDate = entity.getModifiedDate();
    }
}
