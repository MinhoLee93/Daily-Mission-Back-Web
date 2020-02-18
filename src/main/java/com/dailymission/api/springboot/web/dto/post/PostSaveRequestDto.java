package com.dailymission.api.springboot.web.dto.post;

import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.post.Post;
import com.dailymission.api.springboot.web.repository.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class PostSaveRequestDto  {
    private Long missionId;
    private String title;
    private String content;
    private MultipartFile file;


    @Builder
    public PostSaveRequestDto(Long missionId, String title, String content, MultipartFile file){
        this.missionId = missionId;
        this.title = title;
        this.content = content;
        this.file = file;
    }

    public Post toEntity(User user, Mission mission){

        return Post.builder()
                .mission(mission)
                .user(user)
                .title(title)
                .content(content)
                .build();
    }
}
