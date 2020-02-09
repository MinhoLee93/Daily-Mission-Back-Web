package com.dailymission.api.springboot.web.dto.participant;

import com.dailymission.api.springboot.web.repository.account.Account;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ParticipantUpdateRequestDto {
    private Mission mission;
    private Account account;

    @Builder
    public ParticipantUpdateRequestDto(Mission mission, Account account){
        this.mission = mission;
        this.account = account;
    }
}
