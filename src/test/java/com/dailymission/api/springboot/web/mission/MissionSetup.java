package com.dailymission.api.springboot.web.mission;

import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import com.dailymission.api.springboot.web.repository.mission.rule.Week;
import com.dailymission.api.springboot.web.repository.user.User;
import lombok.Builder;

import java.time.LocalDate;

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
                .sun(true)
                .mon(true)
                .tue(true)
                .wed(true)
                .thu(true)
                .fri(false)
                .sat(false).build();

        MissionRule missionRule = MissionRule.builder().week(week).build();

        // date
        LocalDate startDate = LocalDate.of(2020,01,01);
        LocalDate endDate = LocalDate.of(2020,03,31);

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
