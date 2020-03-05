package com.dailymission.api.springboot.web.dto.post;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class PostDeleteResponseDto implements Serializable {
    private Long id;

    @Builder
    public PostDeleteResponseDto(Long id){
        this.id = id;
    }
}
