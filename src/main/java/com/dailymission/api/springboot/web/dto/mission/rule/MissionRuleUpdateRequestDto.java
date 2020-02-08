package com.dailymission.api.springboot.web.dto.mission.rule;

import com.dailymission.api.springboot.web.repository.mission.rule.Week;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MissionRuleUpdateRequestDto {
    private Week week;

    @Builder
    public MissionRuleUpdateRequestDto(Week week){
        this.week = week;
    }
}
