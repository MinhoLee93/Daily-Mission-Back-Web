package com.dailymission.api.springboot.web.controller;


import com.dailymission.api.springboot.web.dto.PostsResponseDto;
import com.dailymission.api.springboot.web.dto.PostsSaveRequestDto;
import com.dailymission.api.springboot.web.dto.PostsUpdateRequestDto;
import com.dailymission.api.springboot.web.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostsApiController {

    private final PostsService postsService;

    @PostMapping("/api/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto){
        return postsService.save(requestDto);
    }

    @PutMapping("/api/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto){
        return postsService.update(id, requestDto);
    }

    @GetMapping("/api/posts/{id}")
    public PostsResponseDto findById (@PathVariable Long id){
        return postsService.findById(id);
    }
}
