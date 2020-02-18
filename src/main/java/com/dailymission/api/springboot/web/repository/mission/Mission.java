package com.dailymission.api.springboot.web.repository.mission;

import com.dailymission.api.springboot.web.repository.common.BaseTimeEntity;
import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import com.dailymission.api.springboot.web.repository.participant.Participant;
import com.dailymission.api.springboot.web.repository.post.Post;
import com.dailymission.api.springboot.web.repository.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
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
    @JsonManagedReference
    private MissionRule missionRule;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(mappedBy = "mission")
    @JsonBackReference
    private List<Participant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "mission")
    private List<Post> posts = new ArrayList<>();

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "CONTENT", nullable = false)
    private String content;

    @Column(name="IMAGE_URL", nullable = false)
    private String imageUrl;

    @Column(name="THUMBNAIL_URL", nullable = false)
    private String thumbnailUrl;

    @Column(name = "CREDENTIAL", nullable = false)
    private String credential;

    @Column(name = "START_DATE", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Column(name = "END_DATE", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Column(name = "ENDED", nullable = false)
    private boolean ended;

    @Column(name = "DELETED", nullable = false)
    private boolean deleted;

    @Builder
    public Mission(MissionRule missionRule, User user, String title, String content,
                   String imageUrl, LocalDate startDate, LocalDate endDate){
        this.missionRule = missionRule;
        this.user = user;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;

        // credential
        this.credential = createCredential();

        // s3
        this.imageUrl = imageUrl;
        this.thumbnailUrl = imageUrl;

        this.ended = false;
        this.deleted = false;
    }

    // 비밀번호 생성
    private String createCredential(){
        String genId = UUID.randomUUID().toString();
        genId = genId.replace("-","");

        return genId;
    }

    // 비밀번호 확인

    public boolean checkCredential(String credential){
        if(!this.credential.equals(credential)){
            return false;
        }else{
            return true;
        }
    }

    // 종료 및 삭제여부 확인
    public boolean checkStatus(){
        if(this.ended || this.deleted){
            return false;
        }else{
            return true;
        }
    }

    // 시작날짜 확인
    public boolean checkStartDate(LocalDate date){
        if(date.isAfter(this.startDate)){
            return false;
        }else{
            return true;
        }
    }

    // 이미지 변경
    public void updateImage(String imageUrl){
        // 이미지
        this.imageUrl = imageUrl;
        // 썸네일 -> 재생성
        this.thumbnailUrl = imageUrl;
    }

    // 썸네일 업데이트
    public void updateThumbnail(String thumbnailUrl){
        this.thumbnailUrl = thumbnailUrl;
    }

    // 업데이트
    public void update(MissionRule missionRule, String title, String content, LocalDate startDate, LocalDate endDate) {
        this.missionRule = missionRule;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // 삭제
    public void delete(User user){
        if(this.user.getId() != user.getId()){
            throw new IllegalArgumentException("허용되지 않은 유저입니다.");
        }

        this.deleted = true;
        this.ended = true;
    }

    // 종료
    public void end(){
        this.ended = true;
    }
}
