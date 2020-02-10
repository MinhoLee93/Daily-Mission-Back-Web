package com.dailymission.api.springboot.web.controller.post;

import com.dailymission.api.springboot.web.dto.post.PostListResponseDto;
import com.dailymission.api.springboot.web.dto.post.PostSaveRequestDto;
import com.dailymission.api.springboot.web.dto.post.PostUpdateRequestDto;
import com.dailymission.api.springboot.web.service.post.PostService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping("/api/post")
    public Long save(@RequestPart("requestJson") String requestJson, @RequestPart("file") MultipartFile file) throws Exception{
        PostSaveRequestDto requestDto =  new Gson().fromJson(requestJson, PostSaveRequestDto.class);

        return postService.save(requestDto, file);
    }

    @GetMapping("/api/post/{id}")
    public String findById (@PathVariable Long id){
        String json = new Gson().toJson(postService.findById(id));

        return json;
    }

    @PutMapping("/api/post/{id}")
    public Long update(@PathVariable Long id, @RequestBody String requestJson){
        PostUpdateRequestDto requestDto =  new Gson().fromJson(requestJson, PostUpdateRequestDto.class);

        return postService.update(id, requestDto);
    }

    @PutMapping("/api/post/{id}/image")
    public Long updateImage(@PathVariable Long id, @RequestPart("file") MultipartFile file) throws IOException {

        return postService.updateImage(id, file);
    }

    @DeleteMapping("/api/post/{id}")
    public Long delete(@PathVariable Long id){
        postService.delete(id);

        return id;
    }

    @GetMapping("/api/post/all")
    public String all(){
        List<PostListResponseDto> responseDtoList =  postService.findAllDesc();
        String json = new Gson().toJson(responseDtoList);

        return json;
    }

}
