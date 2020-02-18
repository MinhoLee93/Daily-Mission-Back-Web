package com.dailymission.api.springboot.web.dto.mission;

import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import com.dailymission.api.springboot.web.repository.user.User;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
public class MissionResponseDto {
    private Long id;
    private MissionRule missionRule;
    private User user;
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
        this.missionRule = entity.getMissionRule();
        this.user = entity.getUser();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.imageUrl = entity.getImageUrl();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.ended = entity.isEnded();
    }
}
