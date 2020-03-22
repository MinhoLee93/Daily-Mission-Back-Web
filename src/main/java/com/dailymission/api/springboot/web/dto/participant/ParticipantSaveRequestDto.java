package com.dailymission.api.springboot.web.dto.participant;

import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.participant.Participant;
import com.dailymission.api.springboot.web.repository.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class ParticipantSaveRequestDto {
    private Mission mission;
    @NotNull
    private String credential;

    @Builder
    public ParticipantSaveRequestDto(Mission mission, String credential){
        this.mission = mission;
        this.credential = credential;
    }

    public Participant toEntity(User user){
        return Participant.builder()
                          .user(user)
                          .mission(mission)
                          .build();
    }
}
