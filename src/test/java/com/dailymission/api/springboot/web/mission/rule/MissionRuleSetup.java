package com.dailymission.api.springboot.web.mission.rule;

import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import com.dailymission.api.springboot.web.repository.mission.rule.Week;
import lombok.Builder;

public class MissionRuleSetup {

    @Builder
    public MissionRuleSetup(){

    }

    public MissionRule get(){
        return buildMissionRule();
    }

    private MissionRule buildMissionRule(){
        Week week = Week.builder()
                .sun(true)
                .mon(true)
                .tue(true)
                .wed(true)
                .thu(true)
                .fri(false)
                .sat(false).build();

        return MissionRule.builder()
                          .week(week)
                          .build();
    }
}
