package com.dailymission.api.springboot.web.controller.participant;

import com.dailymission.api.springboot.security.CurrentUser;
import com.dailymission.api.springboot.security.UserPrincipal;
import com.dailymission.api.springboot.web.dto.participant.ParticipantSaveRequestDto;
import com.dailymission.api.springboot.web.service.participant.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ParticipantController {
    // service
    private final ParticipantService participantService;

    /**
     * [ 2020-03-11 : 이민호 ]
     * 설명 : 미션에 참여한다.
     * */
    @PostMapping("/api/participant")
    @PreAuthorize("hasRole('USER')")
    @Caching(evict = {
            @CacheEvict(value = "users", key = "#userPrincipal.id"),
            @CacheEvict(value = "missionLists", key = "'hot'"),
            @CacheEvict(value = "missionLists", key = "'new'"),
            @CacheEvict(value = "missionLists", key = "'all'"),
            @CacheEvict(value = "missions", key = "#requestDto.mission.id"),
            @CacheEvict(value = "schedules", key = "'mission-' + #requestDto.mission.id + '-week-0'")
    })
    public boolean save(@RequestBody ParticipantSaveRequestDto requestDto, @CurrentUser UserPrincipal userPrincipal){

        return participantService.save(requestDto, userPrincipal);
    }

}
