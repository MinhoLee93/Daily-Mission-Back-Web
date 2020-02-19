package com.dailymission.api.springboot.web.dto.post;

import com.dailymission.api.springboot.web.dto.mission.MissionUserListResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
public class PostScheduleResponseDto {
    private List<MissionUserListResponseDto> users;
    private Map<LocalDate, List<MissionUserListResponseDto>> schedules;

    @Builder
    public PostScheduleResponseDto(List<MissionUserListResponseDto> users, Map<LocalDate, List<MissionUserListResponseDto>> schedules){

        // 미션 참여 유저 목록 (강퇴여부 포함 / 스케줄 왼쪽)
        this.users = users;

        // 일주일 스케줄 목록
        this.schedules = schedules;

    }

}
