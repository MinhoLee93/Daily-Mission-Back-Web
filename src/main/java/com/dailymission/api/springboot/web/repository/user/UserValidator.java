package com.dailymission.api.springboot.web.repository.user;

import lombok.Getter;

/**
 * [ 2020-03-18 : 이민호 ]
 * 설명 : USER Data Validation Field
 * */
@Getter
public class UserValidator {
    public static final int USER_NAME_MIN_LENGTH = 1;
    public static final int USER_NAME_MAX_LENGTH = 20;
}
