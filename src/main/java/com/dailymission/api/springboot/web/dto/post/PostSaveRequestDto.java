package com.dailymission.api.springboot.web.dto.post;

import com.dailymission.api.springboot.web.repository.account.Account;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostSaveRequestDto  {
    private Mission mission;
    private Account account;
    private String title;
    private String content;

    @Builder
    public PostSaveRequestDto(Mission mission, Account account, String title, String content){
        this.mission = mission;
        this.account = account;
        this.title = title;
        this.content = content;
    }

    public Post toEntitiy(){
        return Post.builder()
                .mission(mission)
                .account(account)
                .title(title)
                .content(content)
                .build();
    }
}
