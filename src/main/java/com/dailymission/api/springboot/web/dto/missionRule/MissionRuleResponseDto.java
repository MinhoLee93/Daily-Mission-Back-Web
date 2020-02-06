package com.dailymission.api.springboot.web.dto.missionRule;

import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import com.dailymission.api.springboot.web.repository.mission.rule.Week;
import lombok.Getter;

@Getter
public class MissionRuleResponseDto {
    private Long id;
    private Week week;

    public MissionRuleResponseDto(MissionRule entity){
        this.id = entity.getId();
        this.week = entity.getWeek();
    }
}
