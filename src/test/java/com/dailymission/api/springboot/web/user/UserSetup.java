package com.dailymission.api.springboot.web.user;


import com.dailymission.api.springboot.web.repository.user.AuthProvider;
import com.dailymission.api.springboot.web.repository.user.User;
import lombok.Builder;


public class UserSetup {

//    private String name;
    private final String THUMBNAIL_URL = "THUMBNAIL_URL.jpg";
    private final String USER_NAME = "USER_NAME";
    private final String EMAIL = "EMAIL@gmail.com";
    private final String IMAGE_URL = "IMAGE_URL.jpg";
    private final AuthProvider PROVIDER = AuthProvider.google;
    private final String PROVIDER_ID = "PROVIDER_ID";

    @Builder
    public UserSetup(){

    }

    public User get(){
        return buildUser();
    }

    private User buildUser() {

        User user =  User.builder()
                            .name(USER_NAME)
                            .email(EMAIL)
                            .imageUrl(IMAGE_URL)
                            .provider(PROVIDER)
                            .providerId(PROVIDER_ID)
                            .build();

        user.setThumbnailUrl(THUMBNAIL_URL);

        return user;
    }
}
