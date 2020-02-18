package com.dailymission.api.springboot.web.dto.mission;

import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import com.dailymission.api.springboot.web.repository.mission.rule.Week;
import com.dailymission.api.springboot.web.repository.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class MissionSaveRequestDto {
    private Week week;
    private String title;
    private String content;
    private MultipartFile file;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Builder
    public MissionSaveRequestDto(Week week, String title, String content, MultipartFile file, LocalDate startDate, LocalDate endDate){
        this.week = week;
        this.title = title;
        this.content = content;
        this.file = file;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Mission toEntity(User user){
        MissionRule missionRule = MissionRule.builder().week(week).build();

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
