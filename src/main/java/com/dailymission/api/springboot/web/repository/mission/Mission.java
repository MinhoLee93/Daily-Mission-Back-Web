package com.dailymission.api.springboot.web.repository.mission;

import com.dailymission.api.springboot.web.repository.common.BaseTimeEntity;
import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import com.dailymission.api.springboot.web.repository.post.Post;
import com.dailymission.api.springboot.web.repository.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
public class Mission extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

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
    public Mission(String title, String content, Date startDate, Date endDate, String endFlag, String deleteFlag){
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;

        this.credential = createCredential();
    }

    private String createCredential(){
        String genId = UUID.randomUUID().toString();
        genId = genId.replace("-","");

        return genId;
    }


    @OneToOne
    @JoinColumn(name = "MISSION_RULE_ID")
    private MissionRule missionRule;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(mappedBy = "mission")
    private List<Post> posts = new ArrayList<>();
}
