package com.dailymission.api.springboot.web.dto.participant;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ParticipantSaveResponseDto {
    private Long id;

    @Builder
    public ParticipantSaveResponseDto(Long id){
        this.id = id;
    }
}
