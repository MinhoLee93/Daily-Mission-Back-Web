package com.dailymission.api.springboot.web.dto.post;

import com.dailymission.api.springboot.web.repository.user.User;
import com.dailymission.api.springboot.web.repository.post.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostListResponseDto {
    private Long id;
    private String title;
    private User user;
    private String imagePath;
    private LocalDateTime modifiedDate;

    public PostListResponseDto(Post entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.user = entity.getUser();
        this.imagePath = entity.getImagePath();
        this.modifiedDate = entity.getModifiedDate();
    }
}
