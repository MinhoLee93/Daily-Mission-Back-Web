package com.dailymission.api.springboot.web.dto.mission;

import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.rule.Week;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
public class MissionResponseDto {
    private Long id;
    private Week week;
    private Long userId;
    private String userName;
    private String title;
    private String content;
    private String imageUrl;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private boolean ended;

    public MissionResponseDto(Mission entity){
        this.id = entity.getId();
        this.week = entity.getMissionRule().getWeek();
        this.userId = entity.getUser().getId();
        this.userName = entity.getUser().getName();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.imageUrl = entity.getImageUrl();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.ended = entity.isEnded();
    }
}
