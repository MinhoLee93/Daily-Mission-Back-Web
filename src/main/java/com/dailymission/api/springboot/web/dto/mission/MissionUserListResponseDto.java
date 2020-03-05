package com.dailymission.api.springboot.web.dto.mission;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

/*
* 특정 미션에 참여중인 전체 유저 목록 (강퇴여부 포함)
* */
@Getter
public class MissionUserListResponseDto implements Serializable {
    private Long userId;
    private String userName;
    private boolean banned;

    @Builder
    public MissionUserListResponseDto(Long userId, String userName, boolean banned){
        this.userId = userId;
        this.userName = userName;
        this.banned = banned;
    }
}
