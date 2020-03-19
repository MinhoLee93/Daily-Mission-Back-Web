package com.dailymission.api.springboot.web.post;

import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.MissionRepository;
import com.dailymission.api.springboot.web.repository.post.Post;
import com.dailymission.api.springboot.web.repository.post.PostRepository;
import com.dailymission.api.springboot.web.repository.user.UserRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PostRepositoryTest {

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    private Post post;

    private Mission mission;
//
//    @Before
//    public void setup(){
//        // 미션 생성 유저 정보
//        UserSetup userSetup = UserSetup.builder()
//                                                .name("미션 생성자")
//                                                .build();
//        User missionCreator = userRepository.save(userSetup.build());
//
//        // 미션
//        MissionSetup missionSetup = MissionSetup.builder()
//                                                .user(missionCreator)
//                                                .build();
//        mission = missionRepository.save(missionSetup.build());
//
//        // 포스트 생성 유저 정보
//        userSetup = UserSetup.builder()
//                                    .name("포스트 생성자")
//                                    .build();
//        User postCreator = userRepository.save(userSetup.build());
//
//        // 포스트
//        PostSetup postSetup = PostSetup.builder()
//                                        .mission(mission)
//                                        .user(postCreator)
//                                        .build();
//    }
//
//    @Test(expected = InvalidDataAccessApiUsageException.class)
//    public void post_save_할때_존재하지_않는_mission_에러가_발생하는지(){
//        // given
//        missionRepository.delete(mission);
//
//        // when
//        postRepository.save(post);
//    }
//
//    @Test(expected = NullPointerException.class)
//    public void post_save_할때_존재하지_않는_Account_에러가_발생하는지(){
//        // given
//        userRepository.delete(post.getUser());
//
//        // when
//        postRepository.save(post);
//    }
}
