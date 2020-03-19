package com.dailymission.api.springboot.web.mission;

import com.dailymission.api.springboot.web.repository.mission.MissionRepository;
import com.dailymission.api.springboot.web.repository.mission.rule.MissionRuleRepository;
import com.dailymission.api.springboot.web.repository.user.UserRepository;
import com.dailymission.api.springboot.web.user.UserSetup;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MissionRepositoryTest {

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MissionRuleRepository missionRuleRepository;


    private MissionSetup missionSetup;

    private UserSetup userSetup;


//    @Before
//    public void setup(){
//        userSetup = UserSetup.builder().build();
//
//        User user = userRepository.save(userSetup.build());
//
//        missionSetup = MissionSetup.builder().user(user).build();
//    }
//
//    @Test
//    public void mission_save_할때_missionRule도_함께_생성이되는지(){
//        // when
//        Mission saved = missionRepository.save(missionSetup.build());
//
//        // then
//        assertThat(saved.getMissionRule()).isNotNull();
//        assertThat(saved.getMissionRule().getWeek()).isNotNull();
//    }
//
//    @Test
//    public void mission_delete_할때_missionRule도_함께_제거되는지(){
//        // when
//        Mission saved = missionRepository.save(missionSetup.build());
//        missionRepository.delete(saved);
//
//        boolean existMission = missionRepository.existsById(saved.getId());
//        boolean existMissionRule = missionRuleRepository.existsById(saved.getMissionRule().getId());
//
//        // then
//        assertThat(existMission).isFalse();
//        assertThat(existMissionRule).isFalse();
//    }
}
