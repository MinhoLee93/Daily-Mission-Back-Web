package com.dailymission.api.springboot.exception;

import org.springframework.security.core.AuthenticationException;

/*
 * The following exception classes are used throughout the application for various error cases -
 * */
public class OAuth2AuthenticationProcessingException extends AuthenticationException {
    public OAuth2AuthenticationProcessingException(String msg, Throwable t) {
        super(msg, t);
    }

    public OAuth2AuthenticationProcessingException(String msg) {
        super(msg);
    }
}
