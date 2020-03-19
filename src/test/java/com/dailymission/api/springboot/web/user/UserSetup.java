package com.dailymission.api.springboot.web.user;


import com.dailymission.api.springboot.web.repository.user.AuthProvider;
import com.dailymission.api.springboot.web.repository.user.User;
import lombok.Builder;


public class UserSetup {

    private String name;
    private final String THUMBNAIL_URL = "THUMBNAIL_URL.jpg";

    @Builder
    public UserSetup(){

    }

    public User get(){
        return buildUser();
    }

    private User buildUser() {

        User user =  User.builder()
                            .name("USER_NAME")
                            .email("EMAIL@gmail.com")
                            .imageUrl("IMAGE_URL.jpg")
                            .provider(AuthProvider.google)
                            .providerId("PROVIDER_ID")
                            .build();

        user.setThumbnailUrl(THUMBNAIL_URL);

        return user;
    }
}
