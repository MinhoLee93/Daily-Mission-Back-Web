package com.dailymission.api.springboot.web.dto.post;

import com.dailymission.api.springboot.web.repository.post.Post;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
public class PostListResponseDto {
    private Long id;
    private String userName;
    private String title;
    private String content;
    private String thumbnailUrl;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime modifiedDate;

    public PostListResponseDto(Post entity){
        this.id = entity.getId();
        this.userName = entity.getUser().getName();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.thumbnailUrl = entity.getThumbnailUrl();
        this.modifiedDate = entity.getModifiedDate();
    }
}
