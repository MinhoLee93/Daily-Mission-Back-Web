package com.dailymission.api.springboot.web.dto.missionRule;

import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import com.dailymission.api.springboot.web.repository.mission.rule.Week;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MissionRuleSaveRequestDto {
    private Week week;

    @Builder
    public MissionRuleSaveRequestDto(Week week){
        this.week = week;
    }

    public MissionRule toEntitiy(){
        return MissionRule.builder()
                .week(week)
                .build();
    }
}
