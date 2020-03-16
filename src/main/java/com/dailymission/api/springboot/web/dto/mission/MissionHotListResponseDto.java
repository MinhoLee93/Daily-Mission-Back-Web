package com.dailymission.api.springboot.web.dto.mission;

import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.rule.Week;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class MissionHotListResponseDto  implements Serializable {
    private Long id;
    private String title;
    private String content;
    private Week week;
    private String thumbnailUrlHot;
    private String userName;
    private String userThumbnailUrl;
    private int userCount;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private boolean ended;

    public MissionHotListResponseDto(Mission entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.week = entity.getMissionRule().getWeek();
        this.thumbnailUrlHot = entity.getThumbnailUrlHot();
        this.userName = entity.getUser().getName();
        this.userThumbnailUrl = entity.getUser().getThumbnailUrl();
        this.userCount = entity.getParticipantCountNotBanned();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.ended = entity.isEnded();
    }
}
