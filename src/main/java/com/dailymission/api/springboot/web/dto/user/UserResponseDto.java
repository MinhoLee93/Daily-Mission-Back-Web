package com.dailymission.api.springboot.web.dto.user;

import com.dailymission.api.springboot.web.dto.mission.MissionMockDto;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
public class UserResponseDto implements Serializable {

    private Long id;
    private String name;
    private String email;
    private String thumbnailUrl;
    private List<MissionMockDto> missions = new ArrayList<>();

    @Builder
    public UserResponseDto(Long id, String name, String email, String thumbnailUrl){
        this.id = id;
        this.name = name;
        this.email = email;
        this.thumbnailUrl = thumbnailUrl;
    }

    public void addMissionMock(MissionMockDto missionMock){
        missions.add(missionMock);
    }
}
