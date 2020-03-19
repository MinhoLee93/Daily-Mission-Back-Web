package com.dailymission.api.springboot.web.controller.user;

import com.dailymission.api.springboot.security.CurrentUser;
import com.dailymission.api.springboot.security.UserPrincipal;
import com.dailymission.api.springboot.web.dto.user.UserResponseDto;
import com.dailymission.api.springboot.web.dto.user.UserUpdateRequestDto;
import com.dailymission.api.springboot.web.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/*
* The UserController class contains a protected API to get the details of the currently authenticated user
* */
@RestController
@RequiredArgsConstructor
public class UserController {
    // service
    private final UserService userService;

    /**
     * [ 2020-03-11 : 이민호 ]
     * 설명 : 본인의 유저 정보를 가져온다. (아이디, 이름, 이메일, 썸네일, 참여중인 미션 목록 및 당일 포스트 제출여부)
     * */
    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    @Cacheable(value = "users", key = "#userPrincipal.id")
    public UserResponseDto getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {

        return userService.getCurrentUser(userPrincipal);
    }

    /**
     * [ 2020-03-11 : 이민호 ]
     * 설명 : 유저의 정보를 업데이트 한다. (이름, 이미지)
     * */
    @PostMapping("/user/me/update")
    @PreAuthorize("hasRole('USER')")
    @CacheEvict(value = "users", key = "#userPrincipal.id")
    public Long updateUser(@RequestBody UserUpdateRequestDto requestDto, @CurrentUser UserPrincipal userPrincipal) throws IOException {

        // update
        userService.updateUser(requestDto, userPrincipal);

        return userPrincipal.getId();
    }
}