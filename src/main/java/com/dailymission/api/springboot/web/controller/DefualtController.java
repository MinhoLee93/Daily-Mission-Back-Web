package com.dailymission.api.springboot.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefualtController {

    @GetMapping("/")
    public String test(){
        return "Hello World";
    }
}
