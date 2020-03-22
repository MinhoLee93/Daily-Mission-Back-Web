package com.dailymission.api.springboot.web.participant;

import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.participant.Participant;
import com.dailymission.api.springboot.web.repository.user.User;
import lombok.Builder;

public class ParticipantSetup {

    private Mission mission;
    private User user;

    @Builder
    public ParticipantSetup(Mission mission, User user){
        this.mission = mission;
        this.user = user;
    }

    public Participant get(){
        return buildParticipant();
    }

    private Participant buildParticipant() {
        return Participant.builder()
                        .mission(mission)
                        .user(user)
                        .build();
    }
}
