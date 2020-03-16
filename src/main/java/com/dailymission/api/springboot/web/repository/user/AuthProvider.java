package com.dailymission.api.springboot.web.repository.user;


/*
* The User class contains information about the authentication provider.
* Following is the definition of the AuthProvider enum -
* */
public enum  AuthProvider {
    local,
    google,
    naver,
    kakao,
    github
}