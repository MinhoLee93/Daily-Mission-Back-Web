package com.dailymission.api.springboot.web.controller.participant;

import com.dailymission.api.springboot.web.dto.participant.ParticipantSaveRequestDto;
import com.dailymission.api.springboot.web.service.participant.ParticipantService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ParticipantController {

    private final ParticipantService participantService;

    @PostMapping("/api/participant")
    public Long save(@RequestBody String requestJson){
        ParticipantSaveRequestDto requestDto = new Gson().fromJson(requestJson, ParticipantSaveRequestDto.class);

        return participantService.save(requestDto);
    }

}
