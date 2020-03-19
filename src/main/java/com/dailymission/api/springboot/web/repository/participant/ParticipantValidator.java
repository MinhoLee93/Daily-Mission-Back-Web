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



        return true;
    }
}
