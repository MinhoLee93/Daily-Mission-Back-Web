package com.dailymission.api.springboot.web.dto.mission;

import com.dailymission.api.springboot.web.repository.account.Account;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
public class MissionSaveRequestDto {
    private MissionRule missionRule;
    private Account account;
    private String title;
    private String content;
    private Date startDate;
    private Date endDate;

    @Builder
    public MissionSaveRequestDto(MissionRule missionRule, Account account, String title, String content, Date startDate, Date endDate){
        this.missionRule = missionRule;
        this.account = account;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Mission toEntitiy(){
        return Mission.builder()
                      .missionRule(missionRule)
                      .account(account)
                      .title(title)
                      .content(content)
                      .startDate(startDate)
                      .endDate(endDate)
                      .build();
    }
}
