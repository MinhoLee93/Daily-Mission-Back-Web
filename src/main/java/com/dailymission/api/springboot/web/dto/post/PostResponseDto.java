package com.dailymission.api.springboot.web.dto.post;

import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.post.Post;
import com.dailymission.api.springboot.web.repository.user.User;
import lombok.Getter;

@Getter
public class PostResponseDto {
    private Long id;
    private Mission mission;
    private User user;
    private String title;
    private String content;
    private String imageUrl;

    public PostResponseDto(Post entity){
        this.id = entity.getId();
        this.mission = entity.getMission();
        this.user = entity.getUser();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.imageUrl = entity.getImageUrl();
    }
}
