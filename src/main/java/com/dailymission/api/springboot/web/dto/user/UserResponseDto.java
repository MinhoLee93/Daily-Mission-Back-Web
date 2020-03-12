package com.dailymission.api.springboot.web.dto.user;

import com.dailymission.api.springboot.web.dto.mission.MissionPostSubmitCheckDto;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * [ 2020-03-11 : 이민호 ]
 * 설명 : 유저 정보 + 미션별 포스트 제출여부
 * */
@Getter
public class UserResponseDto implements Serializable {

    private Long id;
    private String name;
    private String email;
    private String thumbnailUrl;
    private List<MissionPostSubmitCheckDto> postSubmitCheckList = new ArrayList<>();

    @Builder
    public UserResponseDto(Long id, String name, String email, String thumbnailUrl){
        this.id = id;
        this.name = name;
        this.email = email;
        this.thumbnailUrl = thumbnailUrl;
    }

    public void addMissionPostSubmitCheckDto(MissionPostSubmitCheckDto postSubmitCheck){
        postSubmitCheckList.add(postSubmitCheck);
    }
}
