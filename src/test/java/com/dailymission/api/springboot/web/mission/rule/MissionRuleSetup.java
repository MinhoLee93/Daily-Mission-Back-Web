package com.dailymission.api.springboot.web.mission.rule;

import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import com.dailymission.api.springboot.web.repository.mission.rule.Week;
import lombok.Builder;

public class MissionRuleSetup {

    @Builder
    public MissionRuleSetup(){

    }

    public MissionRule build(){
        return buildMissionRule();
    }

    private MissionRule buildMissionRule(){
        Week week = Week.builder()
                .sun("Y")
                .mon("Y")
                .tue("Y")
                .wed("Y")
                .thu("Y")
                .fri("Y")
                .sat("Y").build();

        MissionRule missionRule = MissionRule.builder().week(week).build();

        return missionRule;
    }
}
