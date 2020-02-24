package com.dailymission.api.springboot.web.controller.mission;

import com.dailymission.api.springboot.security.CurrentUser;
import com.dailymission.api.springboot.security.UserPrincipal;
import com.dailymission.api.springboot.web.dto.mission.*;
import com.dailymission.api.springboot.web.service.mission.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MissionController {

    private final MissionService missionService;

    @PostMapping("/api/mission")
    @PreAuthorize("hasRole('USER')")
    public MissionSaveResponseDto save(MissionSaveRequestDto requestDto, @CurrentUser UserPrincipal userPrincipal) throws Exception {

         return MissionSaveResponseDto.builder().credential(missionService.save(requestDto, userPrincipal)).build();
    }

    @GetMapping("/api/mission/{id}")
    public MissionResponseDto findById(@PathVariable Long id){

        return missionService.findById(id);
    }

//    @PutMapping("/api/mission/{id}")
//    @PreAuthorize("hasRole('USER')")
//    public Long update(@PathVariable Long id, @RequestBody String requestJson) {
//
//        MissionUpdateRequestDto requestDto = new Gson().fromJson(requestJson, MissionUpdateRequestDto.class);
//
//        return missionService.update(id, requestDto);
//    }

//    @PutMapping("/api/mission/{id}/image")
//    @PreAuthorize("hasRole('USER')")
//    public Long updateImage(@PathVariable Long id, @RequestPart("file") MultipartFile file) throws IOException {
//
//        return missionService.updateImage(id, file);
//    }

    @DeleteMapping("/api/mission/{id}")
    @PreAuthorize("hasRole('USER')")
    public MissionDeleteResponseDto delete(@PathVariable Long id, @CurrentUser UserPrincipal userPrincipal){
        missionService.delete(id, userPrincipal);

        return MissionDeleteResponseDto.builder().id(id).build();
    }

    // 전체
    @GetMapping("/api/mission/all")
    public List<MissionListResponseDto> findAllByCreatedDate(){

        return  missionService.findAllByCreatedDate();
    }

}
