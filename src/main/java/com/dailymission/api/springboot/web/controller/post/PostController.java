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
    // service
    private final PostService postService;

    /**
     * [ 2020-03-13 : 이민호 ]
     * 설명 : 포스트를 저장한다.
     * */
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

    /**
     * [ 2020-03-13 : 이민호 ]
     * 설명 : 포스트의 Detail 정보를 가져온다.
     * */
    @GetMapping("/api/post/{id}")
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


    /**
     * [ 2020-03-13 : 이민호 ]
     * 설명 : 포스트를 삭제한다.
     * */
    @DeleteMapping("/api/post/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    @Caching(evict = {
            // 전체 포스트 List
            @CacheEvict(value = "postLists", key = "'all'"),
            // 유저별 포스트 List
            @CacheEvict(value = "postLists", key = "'user-'+ #userPrincipal.id"),
//            // 미션별 포스트 List
//            @CacheEvict(value = "postLists", key = "'mission-' + #requestDto.missionId"),
//            // 금주 Schedule History
//            @CacheEvict(value = "schedules", key = "'mission-' + #requestDto.missionId + '-week-0'"),
            // 유저 정보
            @CacheEvict(value = "users", key = "#userPrincipal.id"),
            // 포스트 정보 (detail)
            @CacheEvict(value = "posts" , key = "#id")
    })
    public PostDeleteResponseDto delete(@PathVariable Long id, @CurrentUser UserPrincipal userPrincipal){

        // delete post
        postService.delete(id, userPrincipal);

        return PostDeleteResponseDto.builder().id(id).build();
    }


    /**
     * [ 2020-03-13 : 이민호 ]
     * 설명 : 전체 포스트 목록을 가져온다.
     * */
    @GetMapping("/api/post/all")
    @Cacheable(value = "postLists", key = "'all'")
    public List<PostListResponseDto> findAll(){

        return postService.findAll();
    }


    /**
     * [ 2020-03-13 : 이민호 ]
     * 설명 : 유저별 전체 포스트 목록을 가져온다.
     * */
    @GetMapping("/api/post/all/me")
    @PreAuthorize("hasRole('USER')")
    @Cacheable(value = "postLists", key = "'user-' + #userPrincipal.id")
    public List<PostListMyResponseDto> findAllByUser (@CurrentUser UserPrincipal userPrincipal) {

        return postService.findAllByUser(userPrincipal);
    }


    /**
     * [ 2020-03-13 : 이민호 ]
     * 설명 : 미션별 전체 포스트 목록을 가져온다.
     * */
    @GetMapping("/api/post/all/mission/{id}")
    @Cacheable(value = "postLists", key = "'mission-' + #id")
    public List<PostListMissionResponseDto> findAllByMission (@PathVariable Long id) {

        return postService.findAllByMission(id);
    }


    /**
     * [ 2020-03-13 : 이민호 ]
     * 설명 : 미션별로 해당 Week 의 post 제출 schedule 을 가져온다.
     *        week : 0 -> 이번주
     *        week : n -> n 주전
     *
     *        일/월/화/수/목/금/토 정보를 가져온다.
     * */
    @GetMapping("/api/post/schedule/mission/{id}/week/{week}")
    @Cacheable(value = "schedules", key = "'mission-' + #id + '-week-' + #week")
    public PostScheduleResponseDto findSchedule(@PathVariable("id") Long id,
                                                @PathVariable("week") Long week){

        return postService.findSchedule(id, week);
    }
}
