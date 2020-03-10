package com.dailymission.api.springboot.web.dto.user;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserNameImageDto {
    private String userName;
    private String thumbnailUrl;
    private Boolean banned;

    @Builder
    public UserNameImageDto(String userName, String thumbnailUrl, Boolean banned){
        this.userName = userName;
        this.thumbnailUrl = thumbnailUrl;
        this.banned = banned;
    }
}
