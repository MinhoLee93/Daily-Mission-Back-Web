package com.dailymission.api.springboot.web.dto.mission;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MissionDeleteResponseDto {
    private Long id;

    @Builder
    public MissionDeleteResponseDto(Long id){
        this.id = id;
    }
}
