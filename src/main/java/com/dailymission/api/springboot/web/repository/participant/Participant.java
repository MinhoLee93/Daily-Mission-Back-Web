package com.dailymission.api.springboot.web.repository.participant;

import com.dailymission.api.springboot.web.repository.common.BaseTimeEntity;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name="BANNED", nullable = false)
    private boolean banned;

    // 참여
    @Builder
    public Participant(Mission mission, User user){
        this.mission = mission;
        this.user = user;

        this.banned = false;
    }

    // 강퇴
    public void ban(){
        this.banned = true;
    }
}
