package com.dailymission.api.springboot.web.dto.participant;

import com.dailymission.api.springboot.web.repository.user.User;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ParticipantUpdateRequestDto {
    private Mission mission;
    private User user;

    @Builder
    public ParticipantUpdateRequestDto(Mission mission, User user){
        this.mission = mission;
        this.user = user;
    }
}
