package com.dailymission.api.springboot.web.dto.mission;

import com.dailymission.api.springboot.web.dto.user.UserNameImageDto;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.rule.Week;
import com.dailymission.api.springboot.web.repository.participant.Participant;
import com.dailymission.api.springboot.web.repository.user.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class MissionResponseDto implements Serializable {
    private Long id;
    private Week week;
    private String userName;
    private String title;
    private String content;
    private String thumbnailUrlDetail;
    private List<UserNameImageDto> participants = new ArrayList<>();

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

        setParticipants(entity.getParticipants());
    }

    private void setParticipants(List<Participant> participants){
        for(Participant p : participants){
            User user = p.getUser();
            this.participants.add(UserNameImageDto.builder()
                                    .userName(user.getName())
                                    .imageUrl(user.getImageUrl())
                                    .banned(p.isBanned()).build());
        }
    }
}
