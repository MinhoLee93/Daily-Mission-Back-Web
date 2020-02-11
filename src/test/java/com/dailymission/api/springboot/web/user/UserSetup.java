package com.dailymission.api.springboot.web.user;

import com.dailymission.api.springboot.web.repository.user.User;
import lombok.Builder;

public class UserSetup {

    private String name;

    @Builder
    public UserSetup(String name){
        this.name = name;
    }

    public User build(){
        return buildAccount();
    }

    private User buildAccount() {
        return User.builder()
                .email("test")
                .name(name)
                .picture("test")
                .build();
    }
}
