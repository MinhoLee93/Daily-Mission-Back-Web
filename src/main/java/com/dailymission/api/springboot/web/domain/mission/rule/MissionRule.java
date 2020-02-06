package com.dailymission.api.springboot.web.domain.mission.rule;

import com.dailymission.api.springboot.web.domain.common.BaseTimeEntity;
import com.dailymission.api.springboot.web.domain.mission.Mission;
import edu.umd.cs.findbugs.annotations.CleanupObligation;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class MissionRule extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

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

    @Column(name = "DELETE_FLAG", nullable = false)
    @ColumnDefault("'N'")
    private String deleteFlag;

    @Builder
    public MissionRule(String sun, String mon, String tue, String wed, String thu, String fri, String sat){
        this.sun = sun;
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thu = thu;
        this.fri = fri;
        this.sat = sat;

    }

    @OneToOne(mappedBy = "missionRule")
    private Mission mission;
}
