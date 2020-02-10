package com.dailymission.api.springboot.web.dto.mission;

import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import lombok.Getter;

import java.util.Date;

@Getter
public class MissionResponseDto {
    private Long id;
    private MissionRule missionRule;
    private String title;
    private String content;
    private String imagePath;
    private Date startDate;
    private Date endDate;
    private String endFlag;

    public MissionResponseDto(Mission entity){
        this.id = entity.getId();
        this.missionRule = entity.getMissionRule();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.imagePath = entity.getImagePath();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.endFlag = entity.getEndFlag();
    }
}
