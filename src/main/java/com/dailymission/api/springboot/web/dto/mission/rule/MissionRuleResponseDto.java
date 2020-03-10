package com.dailymission.api.springboot.web.dto.mission.rule;

import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import com.dailymission.api.springboot.web.repository.mission.rule.Week;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class MissionRuleResponseDto implements Serializable {
    private Long id;
    private Week week;

    public MissionRuleResponseDto(MissionRule entity){
        this.id = entity.getId();
        this.week = entity.getWeek();
    }
}
