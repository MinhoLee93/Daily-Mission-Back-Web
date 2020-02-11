package com.dailymission.api.springboot.web.dto.post;

import com.dailymission.api.springboot.web.repository.user.User;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostSaveRequestDto  {
    private Mission mission;
    private User user;
    private String title;
    private String content;

    @Builder
    public PostSaveRequestDto(Mission mission, User user, String title, String content){
        this.mission = mission;
        this.user = user;
        this.title = title;
        this.content = content;
    }

    public Post toEntitiy(){
        return Post.builder()
                .mission(mission)
                .user(user)
                .title(title)
                .content(content)
                .build();
    }
}
