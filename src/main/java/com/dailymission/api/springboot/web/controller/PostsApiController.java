package com.dailymission.api.springboot.web.controller;


import com.dailymission.api.springboot.web.dto.PostsListResponseDto;
import com.dailymission.api.springboot.web.dto.PostsResponseDto;
import com.dailymission.api.springboot.web.dto.PostsSaveRequestDto;
import com.dailymission.api.springboot.web.dto.PostsUpdateRequestDto;
import com.dailymission.api.springboot.web.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostsApiController {

    private final PostsService postsService;

    @GetMapping("/api/posts/{id}")
    public PostsResponseDto findById (@PathVariable Long id){
        return postsService.findById(id);
    }

    @PostMapping("/api/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto){
        return postsService.save(requestDto);
    }

    @PutMapping("/api/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto){
        return postsService.update(id, requestDto);
    }

    @DeleteMapping("/api/posts/{id}")
    public Long delete(@PathVariable Long id){
        postsService.delete(id);
        return id;
    }

    @GetMapping("/api/posts/all")
    public List<PostsListResponseDto> all(){
        return postsService.findAllDesc();
    }
}
