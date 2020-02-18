package com.dailymission.api.springboot.web.dto.post;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostDeleteResponseDto {
    private Long id;

    @Builder
    public PostDeleteResponseDto(Long id){
        this.id = id;
    }
}
