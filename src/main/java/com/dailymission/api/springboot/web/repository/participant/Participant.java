package com.dailymission.api.springboot.web.repository.participant;

import com.dailymission.api.springboot.web.dto.participant.ParticipantUserDto;
import com.dailymission.api.springboot.web.repository.common.BaseTimeEntity;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@Entity
public class Participant extends BaseTimeEntity implements Serializable {

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

    /**
     * [ 2020-03-12 : 이민호 ]
     * 설명 : 미션 참여중인 사용자의 이름/사진/강퇴여부를 가져온다
     * */
    private ParticipantUserDto getUserInfo(){

            // user entity
            User user = this.getUser();

            // userName & image & banned
            return ParticipantUserDto.builder()
                    .userName(user.getName())
                    .thumbnailUrl(user.getThumbnailUrl())
                    .banned(this.isBanned()).build();

    }
}
