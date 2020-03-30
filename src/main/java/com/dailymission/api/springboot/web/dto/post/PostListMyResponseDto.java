package com.dailymission.api.springboot.web.dto.post;

import com.dailymission.api.springboot.web.repository.post.Post;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
public class PostListMyResponseDto implements Serializable {
    private Long postId;
    private Long missionId;
    private String missionTitle;
//    private String userName;
//    private String userThumbnailUrl;
    private String title;
    private String content;
//    private String imageUrl;
    private String thumbnailUrlMy;


    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime modifiedDate;

    public PostListMyResponseDto(Post entity){
        this.postId = entity.getId();
        this.missionId = entity.getMission().getId();
        this.missionTitle = entity.getMission().getTitle();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.thumbnailUrlMy = entity.getThumbnailUrlMy();
        this.modifiedDate = entity.getModifiedDate();
    }
}
