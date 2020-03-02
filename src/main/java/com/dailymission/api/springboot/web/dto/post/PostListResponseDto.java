package com.dailymission.api.springboot.web.dto.post;

import com.dailymission.api.springboot.web.repository.post.Post;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
public class PostListResponseDto {
    private Long postId;
    private Long missionId;
    private String missionTitle;
    private String userName;
    private String userImageUrl;
    private String title;
    private String content;
    private String thumbnailUrl;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime modifiedDate;

    public PostListResponseDto(Post entity){
        this.postId = entity.getId();
        this.missionId = entity.getMission().getId();
        this.missionTitle = entity.getMission().getTitle();
        this.userName = entity.getUser().getName();
        this.userImageUrl = entity.getUser().getImageUrl();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.thumbnailUrl = entity.getThumbnailUrl();
        this.modifiedDate = entity.getModifiedDate();
    }
}
