package com.dailymission.api.springboot.web.controller.mission;

import com.dailymission.api.springboot.security.CurrentUser;
import com.dailymission.api.springboot.security.UserPrincipal;
import com.dailymission.api.springboot.web.dto.mission.*;
import com.dailymission.api.springboot.web.service.mission.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MissionController {

    private final MissionService missionService;

    @PostMapping("/api/mission")
    @PreAuthorize("hasRole('USER')")
    @Caching(evict = {
            // 유저 정보
            @CacheEvict(value = "users", key = "#userPrincipal.id"),
            // Home 미션 List
            @CacheEvict(value = "missionLists", key = "'home'"),
            // All 미션 List
            @CacheEvict(value = "missionLists", key = "'all'"),
            // Hot 미션 List
            @CacheEvict(value = "missionLists", key = "'hot'"),
    })
    public MissionSaveResponseDto save(MissionSaveRequestDto requestDto, @CurrentUser UserPrincipal userPrincipal) throws Exception {

         return MissionSaveResponseDto.builder().credential(missionService.save(requestDto, userPrincipal)).build();
    }

    @GetMapping("/api/mission/{id}")
    @Cacheable(value = "missions", key = "#id")
    public MissionResponseDto findById(@PathVariable Long id){

        return missionService.findById(id);
    }


//    @PutMapping("/api/mission/{id}/image")
//    @PreAuthorize("hasRole('USER')")
//    public Long updateImage(@PathVariable Long id, @RequestPart("file") MultipartFile file) throws IOException {
//
//        return missionService.updateImage(id, file);
//    }

    @DeleteMapping("/api/mission/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    @Caching(evict = {
            // 유저 정보
            @CacheEvict(value = "users", key = "#userPrincipal.id"),
            // Home 미션 List
            @CacheEvict(value = "missionLists", key = "'home'"),
            // All 미션 List
            @CacheEvict(value = "missionLists", key = "'all'"),
            // Hot 미션 List
            @CacheEvict(value = "missionLists", key = "'hot'"),
            // 미션 정보 (detail)
            @CacheEvict(value = "missions", key = "#id")
    })
    public MissionDeleteResponseDto delete(@PathVariable Long id, @CurrentUser UserPrincipal userPrincipal){
        missionService.delete(id, userPrincipal);

        return MissionDeleteResponseDto.builder().id(id).build();
    }

    // 홈
    @GetMapping("/api/mission/home")
    @Cacheable(value = "missionLists", key = "'home'")
    public List<MissionHomeListResponseDto> findHomeListByCreatedDate(){

        return  missionService.findHomeListByCreatedDate();
    }

    // 전체
    @GetMapping("/api/mission/all")
    @Cacheable(value = "missionLists", key = "'all'")
    public List<MissionAllListResponseDto> findAllListByCreatedDate(){

        return  missionService.findAllListByCreatedDate();
    }

    // Hot
    @GetMapping("/api/mission/hot")
    @Cacheable(value = "missionLists", key = "'hot'")
    public List<MissionHotListResponseDto> findHotListByCreatedDate(){

        return  missionService.findHotListByCreatedDate();
    }

}
