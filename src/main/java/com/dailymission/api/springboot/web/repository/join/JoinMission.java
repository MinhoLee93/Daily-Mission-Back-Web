package com.dailymission.api.springboot.web.repository.join;

import com.dailymission.api.springboot.web.repository.account.Account;
import com.dailymission.api.springboot.web.repository.common.BaseTimeEntity;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import lombok.Builder;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@IdClass(JoinMissionId.class)
public class JoinMission extends BaseTimeEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "MISSION_ID")
    private Mission mission;

    @Id
    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @Column(name="ATTEND_FLAG", nullable = false)
    @ColumnDefault("'Y'")
    private String attendFlag;

    @Builder
    public JoinMission(Mission mission, Account account){
        this.mission = mission;
        this.account = account;

        this.attendFlag = "Y";
    }

    public void ban(){
        this.attendFlag = "N";
    }
}
