package com.dailymission.api.springboot.web.dto.participant;

import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.participant.Participant;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class ParticipantListResponseDto implements Serializable {
    private Long id;
    private Mission mission;
    private Long userId;
    private String userName;
    private boolean banned;

    public ParticipantListResponseDto(Participant entity){
        this.id = entity.getId();
        this.mission = entity.getMission();
        this.userId = entity.getUser().getId();
        this.userName = entity.getUser().getName();
        this.banned = entity.isBanned();
    }
}
