package com.dailymission.api.springboot.web.controller;

import com.dailymission.api.springboot.web.dto.DefaultDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {

    @GetMapping("/")
    public String test(){
        return "test";
    }

    @GetMapping("/default/dto")
    public DefaultDto testDto(@RequestParam("name") String name, @RequestParam("amount") int amount){
        return new DefaultDto(name, amount);
    }
}
