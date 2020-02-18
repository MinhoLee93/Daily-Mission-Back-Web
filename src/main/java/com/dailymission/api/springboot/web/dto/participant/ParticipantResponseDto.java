package com.dailymission.api.springboot.web.dto.participant;

import com.dailymission.api.springboot.web.repository.user.User;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.participant.Participant;
import lombok.Getter;

@Getter
public class ParticipantResponseDto {
    private Long id;
    private Mission mission;
    private User user;
    private boolean banned;

    public ParticipantResponseDto(Participant entity){
        this.id = entity.getId();
        this.mission = entity.getMission();
        this.user = entity.getUser();
        this.banned = entity.isBanned();
    }
}
