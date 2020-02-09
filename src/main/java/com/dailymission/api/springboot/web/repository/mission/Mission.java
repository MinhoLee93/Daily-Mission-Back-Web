package com.dailymission.api.springboot.web.repository.mission;

import com.dailymission.api.springboot.web.repository.account.Account;
import com.dailymission.api.springboot.web.repository.common.BaseTimeEntity;
import com.dailymission.api.springboot.web.repository.join.JoinMission;
import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import com.dailymission.api.springboot.web.repository.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
public class Mission extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "MISSION_RULE_ID")
    private MissionRule missionRule;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @OneToMany(mappedBy = "mission")
    private List<JoinMission> joinMissions = new ArrayList<>();

    @OneToMany(mappedBy = "mission")
    private List<Post> posts = new ArrayList<>();

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "CONTENT", nullable = false)
    private String content;

    @Column(name = "CREDENTIAL", nullable = false)
    private String credential;

    @Column(name = "START_DATE", nullable = false)
    private Date startDate;

    @Column(name = "END_DATE", nullable = false)
    private Date endDate;

    @Column(name = "END_FLAG", nullable = false)
    @ColumnDefault("'N'")
    private String endFlag;

    @Column(name = "DELETE_FLAG", nullable = false)
    @ColumnDefault("'N'")
    private String deleteFlag;


    @Builder
    public Mission(MissionRule missionRule, Account account, String title, String content, Date startDate, Date endDate, String endFlag){
        this.missionRule = missionRule;
        this.account = account;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;

        // credential
        this.credential = createCredential();

        this.endFlag = "N";
        this.deleteFlag = "N";
    }

    // 비밀번호
    private String createCredential(){
        String genId = UUID.randomUUID().toString();
        genId = genId.replace("-","");

        return genId;
    }

    // 업데이트
    public void update(MissionRule missionRule, String title, String content,  Date startDate, Date endDate) {
        this.missionRule = missionRule;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // 삭제
    public void delete(){
        this.deleteFlag = "Y";
    }

    // 종료
    public void end(){
        this.endFlag = "Y";
    }
}
