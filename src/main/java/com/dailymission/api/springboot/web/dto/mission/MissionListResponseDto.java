package com.dailymission.api.springboot.web.dto.mission;

import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class MissionListResponseDto {
    private Long id;
    private MissionRule missionRule;
    private String title;
    private String content;
    private String thumbnailUrl;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private boolean ended;

    public MissionListResponseDto(Mission entity){
        this.id = entity.getId();
        this.missionRule = entity.getMissionRule();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.thumbnailUrl = entity.getThumbnailUrl();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.ended = entity.isEnded();
    }
}
