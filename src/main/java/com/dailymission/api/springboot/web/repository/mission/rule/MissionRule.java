package com.dailymission.api.springboot.web.repository.mission.rule;

import com.dailymission.api.springboot.web.repository.common.BaseTimeEntity;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@Entity
public class MissionRule extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Embedded Week week;

    @Column(name = "DELETED", nullable = false)
    private boolean deleted;

    @Builder
    public MissionRule(Week week){
        if(week==null){
            throw new IllegalArgumentException("week 값은 필수사항 입니다.");
        }

        this.week = week;
        this.deleted = false;
    }

    @OneToOne(mappedBy = "missionRule")
    @JsonBackReference
    private Mission mission;

    public void update(Week week){

        if(week==null){
            throw new IllegalArgumentException("week 값은 필수사항 입니다.");
        }

        this.week = week;

    }

    public void delete(){
        this.deleted = true;
    }


    // check valid week
    public boolean isValidWeek(){

        // true if all week == false
        if(!(this.week.isSun()
                || this.week.isMon()
                || this.week.isTue()
                || this.week.isWed()
                || this.week.isThu()
                || this.week.isFri()
                || this.week.isSat())){
            throw new IllegalArgumentException("최소 하루의 제출요일을 선택해야 합니다.");
        }

        return true;
    }
}
