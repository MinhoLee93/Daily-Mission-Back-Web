package com.dailymission.api.springboot.web.controller.mission;

import com.dailymission.api.springboot.web.dto.mission.MissionListResponseDto;
import com.dailymission.api.springboot.web.dto.mission.MissionSaveRequestDto;
import com.dailymission.api.springboot.web.dto.mission.MissionUpdateRequestDto;
import com.dailymission.api.springboot.web.service.mission.MissionService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MissionController {

    private final MissionService missionService;

    @PostMapping("/api/mission")
    public Long save(@RequestBody String requestJson){
        MissionSaveRequestDto requestDto = new Gson().fromJson(requestJson, MissionSaveRequestDto.class);

        return missionService.save(requestDto);
    }

    @GetMapping("/api/mission/{id}")
    public String findById(@PathVariable Long id){
        String json  = new Gson().toJson(missionService.findById(id));

        return json;
    }

    @PutMapping("/api/mission/{id}")
    public Long update(@PathVariable Long id, @RequestBody String requestJson){
        MissionUpdateRequestDto requestDto = new Gson().fromJson(requestJson, MissionUpdateRequestDto.class);

        return missionService.update(id, requestDto);
    }

    @DeleteMapping("/api/mission/{id}")
    public Long delete(@PathVariable Long id){
        missionService.delete(id);

        return id;
    }

    @GetMapping("/api/mission/all")
    public String all(){
        List<MissionListResponseDto> responseDtoList = missionService.findAllDesc();
        String json = new Gson().toJson(responseDtoList);

        return json;
    }
}
