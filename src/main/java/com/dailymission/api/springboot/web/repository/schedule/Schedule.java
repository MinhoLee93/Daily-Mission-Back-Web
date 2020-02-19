package com.dailymission.api.springboot.web.repository.schedule;

import com.dailymission.api.springboot.web.dto.mission.MissionUserListResponseDto;
import com.dailymission.api.springboot.web.dto.post.PostHistoryDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class Schedule {
    private List<PostHistoryDto> historyDtos;

    @Builder
    public Schedule(List<PostHistoryDto> historyDtoList){
            this.historyDtos = historyDtoList;
    }

    public Map<LocalDate, List<MissionUserListResponseDto>> getAllSchedule(){
        // schedule
        Map<LocalDate, List<MissionUserListResponseDto>> schedule = new HashMap<>();

        // key (localDate) : value (<MissionUserListResponseDto>)
        for(PostHistoryDto p : historyDtos){
            // userMock (userId, userName)
            MissionUserListResponseDto user = MissionUserListResponseDto.builder()
                    .userId(p.getUserId())
                    .userName(p.getUserName())
                    .build();

            if(schedule.containsKey(p.getDate())){
                schedule.get(p.getDate()).add(user);
            }else{
                List<MissionUserListResponseDto> list = new ArrayList<>();
                list.add(user);
                schedule.put(p.getDate(), list);
            }
        }

        return schedule;
    }
}
