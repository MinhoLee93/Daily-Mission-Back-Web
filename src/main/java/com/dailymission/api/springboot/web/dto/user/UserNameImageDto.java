package com.dailymission.api.springboot.web.dto.user;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserNameImageDto {
    private String userName;
    private String imageUrl;
    private Boolean banned;

    @Builder
    public UserNameImageDto(String userName, String imageUrl, Boolean banned){
        this.userName = userName;
        this.imageUrl = imageUrl;
        this.banned = banned;
    }
}
