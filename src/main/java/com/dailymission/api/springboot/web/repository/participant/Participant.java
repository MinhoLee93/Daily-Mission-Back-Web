package com.dailymission.api.springboot.web.repository.participant;

import com.dailymission.api.springboot.web.repository.user.User;
import com.dailymission.api.springboot.web.repository.common.BaseTimeEntity;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Participant extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MISSION_ID")
    private Mission mission;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name="ATTEND_FLAG", nullable = false)
    @ColumnDefault("'Y'")
    private String attendFlag;

    @Builder
    public Participant(Mission mission, User user){
        this.mission = mission;
        this.user = user;

        this.attendFlag = "Y";
    }

    public void ban(){
        this.attendFlag = "N";
    }
}
