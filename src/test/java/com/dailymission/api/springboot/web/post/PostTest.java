package com.dailymission.api.springboot.web.post;

import com.dailymission.api.springboot.web.user.UserSetup;
import com.dailymission.api.springboot.web.mission.MissionSetup;
import com.dailymission.api.springboot.web.repository.user.User;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.post.Post;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PostTest {

    private Post post;

    @Before
    public void setup(){
        // 미션 생성자
        UserSetup userSetup = UserSetup.builder().name("미션 생성자").build();
        User missionCreator = userSetup.build();

        MissionSetup missionSetup = MissionSetup.builder().user(missionCreator).build();
        Mission mission = missionSetup.build();

        userSetup = UserSetup.builder().name("포스트 작성자").build();
        User postCreator = userSetup.build();
        PostSetup postSetup = PostSetup.builder().mission(mission).user(postCreator).build();
        post = postSetup.build();
    }

    @Test
    public void 생성후_미션_생성자_이름_포스트_작성자_이름_확인(){
        // when
        User missionCreator = post.getMission().getUser();
        User postCreator = post.getUser();

        // then
        assertThat(missionCreator.getName()).isEqualTo("미션 생성자");
        assertThat(postCreator.getName()).isEqualTo("포스트 작성자");
    }

    @Test
    public void 업데이트후_업데이트_내용_확인(){
        // when
        post.update("update","update");

        // then
        assertThat(post.getTitle()).isEqualTo("update");
        assertThat(post.getContent()).isEqualTo("update");
    }
}
