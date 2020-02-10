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
    private String credential;

    @Builder
    public ParticipantSaveRequestDto(Mission mission, Account account, String credential){
        this.mission = mission;
        this.account = account;

        this.credential = credential;
    }

    public Participant toEntity(){
        return Participant.builder()
                          .mission(mission)
                          .account(account)
                          .build();
    }
}
