package com.dailymission.api.springboot.web.controller.post;

import com.dailymission.api.springboot.security.CurrentUser;
import com.dailymission.api.springboot.security.UserPrincipal;
import com.dailymission.api.springboot.web.dto.post.PostDeleteResponseDto;
import com.dailymission.api.springboot.web.dto.post.PostListResponseDto;
import com.dailymission.api.springboot.web.dto.post.PostResponseDto;
import com.dailymission.api.springboot.web.dto.post.PostSaveRequestDto;
import com.dailymission.api.springboot.web.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping("/api/post")
    @PreAuthorize("hasRole('USER')")
    public Long save(PostSaveRequestDto requestDto, @CurrentUser UserPrincipal userPrincipal) throws Exception{

        return postService.save(requestDto, userPrincipal);
    }

    @GetMapping("/api/post/{id}")
    public PostResponseDto findById (@PathVariable Long id) throws Exception {

        return postService.findById(id);
    }

//    @PutMapping("/api/post/{id}")
//    public Long update(@PathVariable Long id, @RequestBody String requestJson){
//        PostUpdateRequestDto requestDto =  new Gson().fromJson(requestJson, PostUpdateRequestDto.class);
//
//        return postService.update(id, requestDto);
//    }
//
//    @PutMapping("/api/post/{id}/image")
//    public Long updateImage(@PathVariable Long id, @RequestPart("file") MultipartFile file) throws IOException {
//
//        return postService.updateImage(id, file);
//    }

    @DeleteMapping("/api/post/{id}")
    @PreAuthorize("hasRole('USER')")
    public PostDeleteResponseDto delete(@PathVariable Long id, @CurrentUser UserPrincipal userPrincipal){
        postService.delete(id, userPrincipal);

        return PostDeleteResponseDto.builder().id(id).build();
    }

    // 전체
    @GetMapping("/api/post/all")
    public List<PostListResponseDto> findAll(){

        return postService.findAll();
    }

    // 개인별
    @GetMapping("/api/post/all/me")
    @PreAuthorize("hasRole('USER')")
    public List<PostListResponseDto> findAllByUser (@CurrentUser UserPrincipal userPrincipal) {

        return postService.findAllByUser(userPrincipal);
    }

    // 미션별
    @GetMapping("/api/post/all/mission/{id}")
    public List<PostListResponseDto> findAllByMission (@PathVariable Long id) {

        return postService.findAllByMission(id);
    }

}
