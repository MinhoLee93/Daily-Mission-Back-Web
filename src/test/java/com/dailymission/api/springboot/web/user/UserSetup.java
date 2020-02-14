package com.dailymission.api.springboot.web.user;


import com.dailymission.api.springboot.web.repository.user.AuthProvider;
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
                .name("test")
                .email("test@google.com")
                .imageUrl("https://s3.ap-northeast-2.amazonaws.com/image.daily-mission.com/default/daily-mission.png")
                .provider(AuthProvider.google)
                .providerId("123456789")
                .build();
    }
}
