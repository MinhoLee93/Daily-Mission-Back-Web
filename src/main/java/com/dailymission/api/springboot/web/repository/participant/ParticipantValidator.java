package com.dailymission.api.springboot.web.repository.participant;

import com.dailymission.api.springboot.web.dto.participant.ParticipantSaveRequestDto;
import lombok.Builder;
import lombok.Getter;


/**
 * [ 2020-03-11 : 이민호 ]
 * 설명 : PARTICIPANT 에 대한 data validation 을 확인한다.
 * */
@Getter
public class ParticipantValidator {

    @Builder
    public ParticipantValidator(){

    }

    public boolean checkValidation(ParticipantSaveRequestDto requestDto) {

        // check mission id
        if(requestDto.getMission().getId()==null){
            throw new IllegalArgumentException("참여할 미션의 아이디가 입력되지 않았습니다.");
        }

        // check credential
        if(requestDto.getCredential()==null){
            throw new IllegalArgumentException("미션에 참여하기위한 비밀번호가 입력되지 않았습니다.");
        }

        return true;
    }
}
