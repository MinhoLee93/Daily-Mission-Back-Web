package com.dailymission.api.springboot.web.dto.post;

import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.post.Post;
import com.dailymission.api.springboot.web.repository.account.Account;
import lombok.Getter;

@Getter
public class PostResponseDto {
    private Long id;
    private Mission mission;
    private Account account;
    private String title;
    private String content;

    public PostResponseDto(Post entity){
        this.id = entity.getId();
        this.mission = entity.getMission();
        this.account = entity.getAccount();
        this.title = entity.getTitle();
        this.content = entity.getContent();
    }
}
