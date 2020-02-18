package com.dailymission.api.springboot.web.dto.post;

import com.dailymission.api.springboot.web.repository.post.Post;
import lombok.Getter;

@Getter
public class PostResponseDto {
    private Long postId;
    private Long missionId;
    private String userName;
    private String title;
    private String content;
    private String imageUrl;

    public PostResponseDto(Post entity){
        this.postId = entity.getId();
        this.missionId = entity.getMission().getId();
        this.userName = entity.getUser().getName();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.imageUrl = entity.getImageUrl();
    }
}
