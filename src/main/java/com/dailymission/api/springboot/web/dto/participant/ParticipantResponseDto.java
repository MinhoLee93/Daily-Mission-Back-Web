package com.dailymission.api.springboot.web.dto.participant;

import com.dailymission.api.springboot.web.repository.participant.Participant;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class ParticipantResponseDto implements Serializable {
    private Long id;
    private Long missionId;
    private Long userId;
    private boolean banned;

    public ParticipantResponseDto(Participant entity){
        this.id = entity.getId();
        this.missionId = entity.getMission().getId();
        this.userId = entity.getUser().getId();
        this.banned = entity.isBanned();
    }
}
