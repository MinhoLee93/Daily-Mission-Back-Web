package com.dailymission.api.springboot.web.repository.schedule;

import com.dailymission.api.springboot.web.dto.mission.MissionUserListResponseDto;
import com.dailymission.api.springboot.web.dto.post.PostSubmitDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class Schedule implements Serializable {
    private List<PostSubmitDto> histories;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Builder
    public Schedule(List<PostSubmitDto> historyDtoList, LocalDate startDate){
            this.histories = historyDtoList;
            this.startDate = startDate;
    }

    public Map<LocalDate, List<MissionUserListResponseDto>> getAllSchedule(){
        // schedule
        Map<LocalDate, List<MissionUserListResponseDto>> schedule = new HashMap<>();

        // create key (일 ~ 토) 비어 있는 hashMap 생성
        for(int i=0; i<7; i++){
            List<MissionUserListResponseDto> list = new ArrayList<>();
            schedule.put(startDate.plusDays(i), list);
        }

        // key (localDate) : value (<MissionUserListResponseDto>)
        for(PostSubmitDto p : histories){
            // userMock (userId, userName)
            MissionUserListResponseDto user = MissionUserListResponseDto.builder()
                    .userId(p.getUserId())
                    .userName(p.getUserName())
                    .build();

            schedule.get(p.getDate()).add(user);
        }

        return schedule;
    }
}
