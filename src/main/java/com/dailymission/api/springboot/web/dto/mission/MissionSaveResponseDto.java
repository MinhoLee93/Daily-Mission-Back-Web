package com.dailymission.api.springboot.web.dto.mission;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MissionSaveResponseDto {
    private String credential;

    @Builder
    public MissionSaveResponseDto(String credential){
        this.credential = credential;
    }
}
