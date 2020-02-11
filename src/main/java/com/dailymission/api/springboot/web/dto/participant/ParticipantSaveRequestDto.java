package com.dailymission.api.springboot.web.dto.participant;

import com.dailymission.api.springboot.web.repository.user.User;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.participant.Participant;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ParticipantSaveRequestDto {
    private Mission mission;
    private User user;
    private String credential;

    @Builder
    public ParticipantSaveRequestDto(Mission mission, User user, String credential){
        this.mission = mission;
        this.user = user;

        this.credential = credential;
    }

    public Participant toEntity(){
        return Participant.builder()
                          .mission(mission)
                          .user(user)
                          .build();
    }
}
