package com.dailymission.api.springboot.web.controller;

import com.dailymission.api.springboot.web.dto.post.PostListResponseDto;
import com.dailymission.api.springboot.web.dto.post.PostSaveRequestDto;
import com.dailymission.api.springboot.web.dto.post.PostUpdateRequestDto;
import com.dailymission.api.springboot.web.service.PostService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class PostApiController {

    private final PostService postService;

    @GetMapping("/api/post/{id}")
    public String findById (@PathVariable Long id){
        String json = new Gson().toJson(postService.findById(id));

        return json;
    }

    @PostMapping("/api/post")
    public Long save(@RequestBody String requestJson){
        PostSaveRequestDto requestDto =  new Gson().fromJson(requestJson, PostSaveRequestDto.class);

        return postService.save(requestDto);
    }

    @PutMapping("/api/post/{id}")
    public Long update(@PathVariable Long id, @RequestBody String requestJson){
        PostUpdateRequestDto requestDto =  new Gson().fromJson(requestJson, PostUpdateRequestDto.class);

        return postService.update(id, requestDto);
    }

    @DeleteMapping("/api/post/{id}")
    public Long delete(@PathVariable Long id){
        postService.delete(id);

        return id;
    }

    @GetMapping("/api/posts/all")
    public String all(){
        List<PostListResponseDto> responseDtoList =  postService.findAllDesc();
        String json = new Gson().toJson(responseDtoList);

        return json;
    }
}
