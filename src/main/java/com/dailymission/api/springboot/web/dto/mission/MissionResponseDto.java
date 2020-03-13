package com.dailymission.api.springboot.web.dto.mission;

import com.dailymission.api.springboot.web.dto.participant.ParticipantUserDto;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.rule.Week;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 * [ 2020-03-12 : 이민호 ]
 * 설명 : 미션 디테일 정보 Dto
 * */
@Getter
public class MissionResponseDto implements Serializable {
    private Long id;
    private Week week;
    private String userName;
    private String title;
    private String content;
    private String thumbnailUrlDetail;
    private List<ParticipantUserDto> participants = new ArrayList<>();

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private boolean ended;

    @Builder
    public MissionResponseDto(Mission entity){
        this.id = entity.getId();
        this.week = entity.getMissionRule().getWeek();
        this.userName = entity.getUser().getName();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.thumbnailUrlDetail = entity.getThumbnailUrlDetail();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.ended = entity.isEnded();
        this.participants = entity.getAllParticipantUser();
    }

}
