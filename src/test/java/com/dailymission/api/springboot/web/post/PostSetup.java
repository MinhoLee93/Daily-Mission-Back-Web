package com.dailymission.api.springboot.web.post;

import com.dailymission.api.springboot.web.repository.user.User;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.post.Post;
import lombok.Builder;

/*
* [포스트 작성 순서]
* 1. User 생성 (MissionSetup 객체안에 들어있음)
* 2. Mission 생성 -> MissionRule 생성
* 3. Post 생성
* */
public class PostSetup {

    private Mission mission;

    private User user;

    @Builder
    /*
    * Mission : 미션
    * User : 작성자
    * */
    public PostSetup(Mission mission, User user){
        this.mission = mission;
        this.user = user;
    }

    public Post build(){
        return buildPost();
    }

    private Post buildPost(){
        return Post.builder()
                    .mission(mission)
                    .user(user)
                    .title("test")
                    .content("test")
                    .build();
    }
}
