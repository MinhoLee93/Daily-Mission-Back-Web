package com.dailymission.api.springboot.web.dto.participant;

import com.dailymission.api.springboot.web.repository.account.Account;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.participant.Participant;
import lombok.Getter;

@Getter
public class ParticipantResponseDto {
    private Long id;
    private Mission mission;
    private Account account;
    private String attendFlag;

    public ParticipantResponseDto(Participant entity){
        this.id = entity.getId();
        this.mission = entity.getMission();
        this.account = entity.getAccount();
        this.attendFlag = entity.getAttendFlag();
    }
}
