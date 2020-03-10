package com.dailymission.api.springboot.web.controller.user;

import com.dailymission.api.springboot.security.CurrentUser;
import com.dailymission.api.springboot.security.UserPrincipal;
import com.dailymission.api.springboot.web.dto.user.UserResponseDto;
import com.dailymission.api.springboot.web.dto.user.UserUpdateRequestDto;
import com.dailymission.api.springboot.web.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

/*
* The UserController class contains a protected API to get the details of the currently authenticated user
* */
@RestController
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    @Cacheable(value = "users", key = "#userPrincipal.id")
    public UserResponseDto getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {

        return userService.getCurrentUser(userPrincipal);
    }

    @PutMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    @CacheEvict(value = "users", key = "#userPrincipal.id")
    public Long updateUser(UserUpdateRequestDto requestDto, @CurrentUser UserPrincipal userPrincipal){

        // update
        userService.updateUser(requestDto, userPrincipal);

        return userPrincipal.getId();
    }
}