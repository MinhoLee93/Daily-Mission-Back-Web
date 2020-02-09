package com.dailymission.api.springboot.web.dto.participant;

import com.dailymission.api.springboot.web.repository.account.Account;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.participant.Participant;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ParticipantSaveRequestDto {
    private Mission mission;
    private Account account;

    @Builder
    public ParticipantSaveRequestDto(Mission mission, Account account){
        this.mission = mission;
        this.account = account;
    }

    public Participant toEntity(){
        return Participant.builder()
                          .mission(mission)
                          .account(account)
                          .build();
    }
}
