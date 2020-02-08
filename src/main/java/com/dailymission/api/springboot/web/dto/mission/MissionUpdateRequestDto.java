package com.dailymission.api.springboot.web.dto.mission;

import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class MissionUpdateRequestDto {
    private MissionRule missionRule;
    private String title;
    private String content;
    private Date startDate;
    private Date endDate;

    @Builder
    public MissionUpdateRequestDto(MissionRule missionRule, String title, String content,  Date startDate, Date endDate)
    {
        this.missionRule = missionRule;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
