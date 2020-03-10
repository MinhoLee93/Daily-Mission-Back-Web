package com.dailymission.api.springboot.web.dto.mission;

import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.participant.Participant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class MissionAllListResponseDto  implements Serializable {
    private Long id;
    private String title;
    private String content;
    private String thumbnailUrlAll;
    private String userName;
    private int userCount;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private boolean ended;

    public MissionAllListResponseDto(Mission entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.thumbnailUrlAll = entity.getThumbnailUrlAll();
        this.userName = entity.getUser().getName();
        this.userCount = count(entity.getParticipants());
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.ended = entity.isEnded();
    }

    // ban 안된 인원 구하기
    public int count(List<Participant> participantList){
        int cnt = 0;
        for(Participant p : participantList){
                if(!p.isBanned()){
                    cnt++;
                }
        }

        return cnt;
    }
}
