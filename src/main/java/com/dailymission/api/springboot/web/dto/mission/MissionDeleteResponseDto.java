package com.dailymission.api.springboot.web.dto.mission;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class MissionDeleteResponseDto  implements Serializable {
    private Long id;

    @Builder
    public MissionDeleteResponseDto(Long id){
        this.id = id;
    }
}
