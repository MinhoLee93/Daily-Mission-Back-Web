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
    // service
    private final MissionService missionService;

    /**
     * [ 2020-03-12 : 이민호 ]
     * 설명 : 신규 미션을 생성한다.
     * */
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

        /**
         * [ 2020-03-12 : 이민호 ]
         * 설명 : 미션 생성이 완료되면, 임의로 생성한 credential 값을 return 한다.
         *        미션에 참여하고 싶은사람은 이 credential 값을 알고 있어야 한다.
         * */
         return MissionSaveResponseDto.builder().credential(missionService.save(requestDto, userPrincipal)).build();
    }


    /**
     * [ 2020-03-12 : 이민호 ]
     * 설명 : 미션의 디테일 정보와 참여중인 사용자들의 이름/사진/강퇴여부를 가져온다
     * */
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


    /**
     * [ 2020-03-12 : 이민호 ]
     * 설명 : 미션을 삭제한다.
     *        삭제 요청은 방장만 할 수 있다.
     *        미션 시작전 & 참여한 사용자가 없을 경우에만 삭제 가능하다.
     * */
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


    /**
     * [ 2020-03-12 : 이민호 ]
     * 설명 : Home 미션 목록을 가져온다.
     * */
    // 홈
    @GetMapping("/api/mission/home")
    @Cacheable(value = "missionLists", key = "'home'")
    public List<MissionHomeListResponseDto> findHomeListByCreatedDate(){

        return  missionService.findHomeListByCreatedDate();
    }


    /**
     * [ 2020-03-12 : 이민호 ]
     * 설명 : 전체 미션 목록을 가져온다.
     * */
    // 전체
    @GetMapping("/api/mission/all")
    @Cacheable(value = "missionLists", key = "'all'")
    public List<MissionAllListResponseDto> findAllListByCreatedDate(){

        return  missionService.findAllListByCreatedDate();
    }


    /**
     * [ 2020-03-12 : 이민호 ]
     * 설명 : Hot 미션 목록을 가져온다.
     * */
    // Hot
    @GetMapping("/api/mission/hot")
    @Cacheable(value = "missionLists", key = "'hot'")
    public List<MissionHotListResponseDto> findHotListByCreatedDate(){

        return  missionService.findHotListByCreatedDate();
    }

}
