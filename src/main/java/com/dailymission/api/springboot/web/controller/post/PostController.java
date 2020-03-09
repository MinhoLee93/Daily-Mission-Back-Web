package com.dailymission.api.springboot.web.controller.post;

import com.dailymission.api.springboot.security.CurrentUser;
import com.dailymission.api.springboot.security.UserPrincipal;
import com.dailymission.api.springboot.web.dto.post.*;
import com.dailymission.api.springboot.web.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping("/api/post")
    @PreAuthorize("hasRole('USER')")
    @Caching(evict = {
            // 전체 포스트 List
            @CacheEvict(value = "postLists", key = "'all'"),
            // 유저별 포스트 List
            @CacheEvict(value = "postLists", key = "'user-'+ #userPrincipal.id"),
            // 미션별 포스트 List
            @CacheEvict(value = "postLists", key = "'mission-' + #requestDto.missionId"),
            // 금주 Schedule History
            @CacheEvict(value = "schedules", key = "'mission-' + #requestDto.missionId + '-week-0'"),
            // 유저 정보
            @CacheEvict(value = "users", key = "#userPrincipal.id"),
    })
    public Long save(PostSaveRequestDto requestDto, @CurrentUser UserPrincipal userPrincipal) throws Exception{

        return postService.save(requestDto, userPrincipal);
    }

    @GetMapping("/api/post/{id}")
    // 포스트 정보 (detail)
    @Cacheable(value = "posts" , key = "#id")
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
    @Caching(evict = {
            // 전체 포스트 List
            @CacheEvict(value = "postLists", key = "'all'"),
            // 유저별 포스트 List
            @CacheEvict(value = "postLists", key = "'user-'+ #userPrincipal.id"),
            // 미션별 포스트 List
            @CacheEvict(value = "postLists", key = "'mission-' + #requestDto.missionId"),
            // 금주 Schedule History
            @CacheEvict(value = "schedules", key = "'mission-' + #requestDto.missionId + '-week-0'"),
            // 유저 정보
            @CacheEvict(value = "users", key = "#userPrincipal.id"),
            // 포스트 정보 (detail)
            @CacheEvict(value = "posts" , key = "#id")
    })
    public PostDeleteResponseDto delete(@PathVariable Long id, @CurrentUser UserPrincipal userPrincipal){
        postService.delete(id, userPrincipal);

        return PostDeleteResponseDto.builder().id(id).build();
    }

    // 전체 포스트 List (key= all)
    @GetMapping("/api/post/all")
    @Cacheable(value = "postLists", key = "'all'")
    public List<PostListResponseDto> findAll(){

        return postService.findAll();
    }

    // 유저별 포스트 List (key = user-#userId)
    @GetMapping("/api/post/all/me")
    @PreAuthorize("hasRole('USER')")
    @Cacheable(value = "postLists", key = "'user-' + #userPrincipal.id")
    public List<PostListResponseDto> findAllByUser (@CurrentUser UserPrincipal userPrincipal) {

        return postService.findAllByUser(userPrincipal);
    }

    // 미션별 포스트 List (key = mission-#missionId)
    @GetMapping("/api/post/all/mission/{id}")
    @Cacheable(value = "postLists", key = "'mission-' + #id")
    public List<PostListResponseDto> findAllByMission (@PathVariable Long id) {

        return postService.findAllByMission(id);
    }

    // 금주 Schedule History / week : 0 (이번주) / week : 1 (1주전) /
    @GetMapping("/api/post/schedule/mission/{id}/{week}")
    @Cacheable(value = "schedules", key = "'mission-' + #id + '-week-' + #week")
    public PostScheduleResponseDto findSchedule(@PathVariable("id") Long id,
                                                @PathVariable("week") Long week){

        return postService.findSchedule(id, week);
    }
}
