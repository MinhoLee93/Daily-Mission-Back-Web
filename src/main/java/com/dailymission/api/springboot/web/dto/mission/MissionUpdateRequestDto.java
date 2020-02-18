package com.dailymission.api.springboot.web.dto.mission;

import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class MissionUpdateRequestDto {
    private MissionRule missionRule;
    private String title;
    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Builder
    public MissionUpdateRequestDto(MissionRule missionRule, String title, String content,  LocalDate startDate, LocalDate endDate)
    {
        this.missionRule = missionRule;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}