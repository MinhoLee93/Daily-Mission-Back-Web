package com.dailymission.api.springboot.web.dto.post;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class PostUpdateRequestDto implements Serializable {
    private String title;
    private String content;

    @Builder
    public PostUpdateRequestDto(String title, String content)
    {
        this.title = title;
        this.content = content;
    }
}
