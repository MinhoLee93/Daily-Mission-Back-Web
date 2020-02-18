package com.dailymission.api.springboot.web.dto.user;

import com.dailymission.api.springboot.web.dto.mission.MissionMockDto;
import com.dailymission.api.springboot.web.repository.participant.Participant;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UserResponseDto {

    private Long id;
    private String name;
    private String email;
    private String imageUrl;
    private List<MissionMockDto> missions = new ArrayList<>();

    @Builder
    public UserResponseDto(Long id, String name, String email, String imageUrl){
        this.id = id;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
    }

    public void filter(List<Participant> participants){
        for(Participant p : participants){
            MissionMockDto missionMock = new MissionMockDto(p.getMission(), p.isBanned());
            this.missions.add(missionMock);
        }
    }
}
