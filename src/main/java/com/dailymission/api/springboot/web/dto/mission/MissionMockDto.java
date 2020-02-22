package com.dailymission.api.springboot.web.dto.mission;

import com.dailymission.api.springboot.web.repository.mission.Mission;
import lombok.Getter;

@Getter
public class MissionMockDto {
    private Long id;
    private String title;
    private boolean banned;

    public MissionMockDto(Mission entity, Boolean banned){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.banned = banned;
    }
}
