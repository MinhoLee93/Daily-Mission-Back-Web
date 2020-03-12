package com.dailymission.api.springboot.web.repository.user;

import com.dailymission.api.springboot.web.dto.user.UserUpdateRequestDto;
import lombok.Builder;
import lombok.Getter;

/**
 * [ 2020-03-11 : 이민호 ]
 * 설명 : USER 에 대한 data validation 을 확인한다.
 * */
@Getter
public class UserValidator {
    private final int USER_NAME_MIN_LENGTH = 1;
    private final int USER_NAME_MAX_LENGTH = 20;

    @Builder
    public UserValidator(){

    }

    public boolean checkValidation(UserUpdateRequestDto requestDto) {

        // check use id
        if(requestDto.getId()==null){
            throw new IllegalArgumentException("변경할 유저의 아이디가 입력되지 않았습니다.");
        }

        // check request data is exist
        if(requestDto.getFile()==null && requestDto.getUserName()==null){
            throw new IllegalArgumentException("변경할 정보가 존재하지 않습니다.");
        }

        // USER_NAME_MIN_LENGTH = 1
        if(requestDto.getUserName().length() < USER_NAME_MIN_LENGTH){
            throw new IllegalArgumentException("유저 이름의 최소 길이는 " + USER_NAME_MIN_LENGTH + "입니다.");
        }

        // USER_NAME_MAX_LENGTH = 20
        if(requestDto.getUserName().length() > USER_NAME_MAX_LENGTH){
            throw new IllegalArgumentException("유저 이름의 최대 길이는 " + USER_NAME_MAX_LENGTH + "입니다.");
        }


        return true;
    }
}
