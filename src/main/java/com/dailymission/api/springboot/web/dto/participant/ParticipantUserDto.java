package com.dailymission.api.springboot.web.dto.participant;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;


/**
 * [ 2020-03-12 : 이민호 ]
 * 설명 : 미션 참여중인 사용자의 이름/사진/강퇴 Dto
 * */
@Getter
public class ParticipantUserDto implements Serializable {
    private Long id;
    private String userName;
    private String thumbnailUrl;
    private Boolean banned;

    @Builder
    public ParticipantUserDto(Long id, String userName, String thumbnailUrl, Boolean banned){
        this.id =  id;
        this.userName = userName;
        this.thumbnailUrl = thumbnailUrl;
        this.banned = banned;
    }
}
