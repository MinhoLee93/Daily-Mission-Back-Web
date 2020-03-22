package com.dailymission.api.springboot.web.mission;

import com.dailymission.api.springboot.web.mission.rule.MissionRuleSetup;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import com.dailymission.api.springboot.web.repository.user.User;
import lombok.Builder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;



public class MissionSetup {

    private final String TITLE = "TITLE";
    private final String CONTENT = "MISSION_CONTENT";
    private final String ORIGINAL_FILE_NAME = "ORIGINAL_FILE_NAME.jpg";
    private final String FILE_EXTENSION = ".jpg";
    private final LocalDate START_DATE = LocalDate.of(2020,04,01);
    private final LocalDate END_DATE = LocalDate.of(2020,04,28);
    private final String IMAGE_URL = "IMAGE_URL.jpg";
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private User user;
    private MissionRule missionRule;

    @Builder
    public MissionSetup(User user){
        this.user = user;
    }

    public Mission get(){
        return buildMission();
    }

    private Mission buildMission(){
        // mission rule (fri/sat :false)
        missionRule = MissionRuleSetup.builder().build().get();

        // mission
        Mission mission = Mission.builder()
                                 .user(user)
                                 .missionRule(missionRule)
                                 .title(TITLE)
                                 .content(CONTENT)
                                 .originalFileName(ORIGINAL_FILE_NAME)
                                 .fileExtension(FILE_EXTENSION)
                                 .imageUrl(IMAGE_URL)
                                 .startDate(START_DATE)
                                 .endDate(END_DATE)
                                 .build();

        // set credential
        mission.setCredential(passwordEncoder);

        // mission
        return mission;
    }
}
