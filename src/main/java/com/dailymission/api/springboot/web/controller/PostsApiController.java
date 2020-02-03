package com.dailymission.api.springboot.web.controller;

import com.dailymission.api.springboot.web.dto.PostsListResponseDto;
import com.dailymission.api.springboot.web.dto.PostsSaveRequestDto;
import com.dailymission.api.springboot.web.dto.PostsUpdateRequestDto;
import com.dailymission.api.springboot.web.service.PostsService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class PostsApiController {

    private final PostsService postsService;

    @GetMapping("/api/posts/{id}")
    public String findById (@PathVariable Long id){
        String json = new Gson().toJson(postsService.findById(id));

        return json;
    }

    @PostMapping("/api/posts")
    public Long save(@RequestBody String requestJson){
        PostsSaveRequestDto requestDto =  new Gson().fromJson(requestJson, PostsSaveRequestDto.class);

        return postsService.save(requestDto);
    }

    @PutMapping("/api/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody String requestJson){
        PostsUpdateRequestDto requestDto =  new Gson().fromJson(requestJson, PostsUpdateRequestDto.class);

        return postsService.update(id, requestDto);
    }

    @DeleteMapping("/api/posts/{id}")
    public Long delete(@PathVariable Long id){
        postsService.delete(id);

        return id;
    }

    @GetMapping("/api/posts/all")
    public String all(){
        List<PostsListResponseDto> responseDtoList =  postsService.findAllDesc();
        String json = new Gson().toJson(responseDtoList);

        return json;
    }
}
