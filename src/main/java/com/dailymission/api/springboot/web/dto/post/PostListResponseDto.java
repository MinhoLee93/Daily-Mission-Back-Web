package com.dailymission.api.springboot.web.dto.post;

import com.dailymission.api.springboot.web.repository.account.Account;
import com.dailymission.api.springboot.web.repository.post.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostListResponseDto {
    private Long id;
    private String title;
    private Account account;
    private LocalDateTime modifiedDate;

    public PostListResponseDto(Post entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.account = entity.getAccount();
        this.modifiedDate = entity.getModifiedDate();
    }
}
