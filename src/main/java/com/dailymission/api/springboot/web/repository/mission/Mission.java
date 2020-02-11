package com.dailymission.api.springboot.web.repository.mission;

import com.dailymission.api.springboot.web.repository.user.User;
import com.dailymission.api.springboot.web.repository.common.BaseTimeEntity;
import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import com.dailymission.api.springboot.web.repository.participant.Participant;
import com.dailymission.api.springboot.web.repository.post.Post;
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "MISSION_RULE_ID")
    private MissionRule missionRule;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(mappedBy = "mission")
    private List<Participant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "mission")
    private List<Post> posts = new ArrayList<>();

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "CONTENT", nullable = false)
    private String content;

    @Column(name="IMAGE_PATH", nullable = false)
    private String imagePath;

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
    public Mission(MissionRule missionRule, User user, String title, String content, Date startDate, Date endDate){
        this.missionRule = missionRule;
        this.user = user;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;

        // credential
        this.credential = createCredential();

        // s3
        this.imagePath = "https://s3.ap-northeast-2.amazonaws.com/image.daily-mission.com/default/daily-mission.png";

        this.endFlag = "N";
        this.deleteFlag = "N";
    }

    // 비밀번호 생성
    private String createCredential(){
        String genId = UUID.randomUUID().toString();
        genId = genId.replace("-","");

        return genId;
    }

    // 비밀번호 확인

    public boolean checkCredential(String credential){
        if(this.credential.equals(credential)){
            return true;
        }else{
            return false;
        }
    }

    // 이미지 변경
    public void updateImage(String imagePath){
        this.imagePath = imagePath;
    }

    // 업데이트
    public void update(MissionRule missionRule, String title, String content, Date startDate, Date endDate) {
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
