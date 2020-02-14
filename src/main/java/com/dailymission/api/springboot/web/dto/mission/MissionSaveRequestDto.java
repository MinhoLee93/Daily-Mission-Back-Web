package com.dailymission.api.springboot.web.dto.mission;

import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import com.dailymission.api.springboot.web.repository.user.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
public class MissionSaveRequestDto {
    private MissionRule missionRule;
    private User user;
    private String title;
    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Builder
    public MissionSaveRequestDto(MissionRule missionRule, User user, String title, String content, LocalDate startDate, LocalDate endDate){
        this.missionRule = missionRule;
        this.user = user;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Mission toEntity(){
        return Mission.builder()
                      .missionRule(missionRule)
                      .user(user)
                      .title(title)
                      .content(content)
                      .startDate(startDate)
                      .endDate(endDate)
                      .build();
    }
}
