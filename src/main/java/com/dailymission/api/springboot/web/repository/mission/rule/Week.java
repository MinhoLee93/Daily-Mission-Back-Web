package com.dailymission.api.springboot.web.repository.mission.rule;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor
public class Week {

    @Column(name = "SUNDAY", nullable = false)
    @ColumnDefault("'Y'")
    private String sun;

    @Column(name = "MONDAY", nullable = false)
    @ColumnDefault("'Y'")
    private String mon;

    @Column(name = "TUESDAY", nullable = false)
    @ColumnDefault("'Y'")
    private String tue;

    @Column(name = "WEDNESDAY", nullable = false)
    @ColumnDefault("'Y'")
    private String wed;

    @Column(name = "THURSDAY", nullable = false)
    @ColumnDefault("'Y'")
    private String thu;

    @Column(name = "FRIDAY", nullable = false)
    @ColumnDefault("'Y'")
    private String fri;

    @Column(name = "SATURDAY", nullable = false)
    @ColumnDefault("'Y'")
    private String sat;

    @Builder
    public Week(String sun, String mon, String tue, String wed, String thu, String fri, String sat){
        this.sun = sun;
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thu = thu;
        this.fri = fri;
        this.sat = sat;
    }

    public void update(String sun, String mon, String tue, String wed, String thu, String fri, String sat){
        this.sun = sun;
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thu = thu;
        this.fri = fri;
        this.sat = sat;
    }
}
