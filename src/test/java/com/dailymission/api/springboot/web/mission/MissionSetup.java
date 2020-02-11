package com.dailymission.api.springboot.web.mission;

import com.dailymission.api.springboot.web.repository.user.User;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import com.dailymission.api.springboot.web.repository.mission.rule.Week;
import lombok.Builder;

import java.util.Calendar;
import java.util.Date;

public class MissionSetup {

    private User user;

    @Builder
    public MissionSetup(User user){
        this.user = user;
    }

    public Mission build(){
        return buildMission();
    }

    private Mission buildMission(){
        Week week = Week.builder()
                .sun("Y")
                .mon("Y")
                .tue("Y")
                .wed("Y")
                .thu("Y")
                .fri("N")
                .sat("N").build();

        MissionRule missionRule = MissionRule.builder().week(week).build();

        // date
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date startDate = new Date();
        Date endDate = cal.getTime();

        // mission
        Mission mission = Mission.builder()
                .title("test")
                .content("test")
                .startDate(startDate)
                .endDate(endDate)
                .missionRule(missionRule)
                .user(user)
                .build();


        return mission;
    }
}
