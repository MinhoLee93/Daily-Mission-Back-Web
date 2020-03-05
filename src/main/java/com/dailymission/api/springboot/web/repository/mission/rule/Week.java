package com.dailymission.api.springboot.web.repository.mission.rule;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class Week implements Serializable {

    @Column(name = "SUNDAY", nullable = false)
    private boolean sun;

    @Column(name = "MONDAY", nullable = false)
    private boolean mon;

    @Column(name = "TUESDAY", nullable = false)
    private boolean tue;

    @Column(name = "WEDNESDAY", nullable = false)
    private boolean wed;

    @Column(name = "THURSDAY", nullable = false)
    private boolean thu;

    @Column(name = "FRIDAY", nullable = false)
    private boolean fri;

    @Column(name = "SATURDAY", nullable = false)
    private boolean sat;

    @Builder
    public Week(boolean sun, boolean mon, boolean tue, boolean wed, boolean thu, boolean fri, boolean sat){
        this.sun = sun;
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thu = thu;
        this.fri = fri;
        this.sat = sat;
    }

    public void update(boolean sun, boolean mon, boolean tue, boolean wed, boolean thu, boolean fri, boolean sat){
        this.sun = sun;
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thu = thu;
        this.fri = fri;
        this.sat = sat;
    }
}
