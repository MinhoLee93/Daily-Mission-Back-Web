package com.dailymission.api.springboot.web.dto.rabbitmq;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MessageDto {
    private Long id;
    private String type;
    private String imageUrl;

    @Builder
    public MessageDto(Long id, String type, String imageUrl){
        this.id = id;
        this.type =type;
        this.imageUrl = imageUrl;
    }
}
