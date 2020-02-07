package com.dailymission.api.springboot.web.repository.post;

import com.dailymission.api.springboot.web.repository.account.Account;
import com.dailymission.api.springboot.web.repository.common.BaseTimeEntity;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;


@Getter
@NoArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITLE", length = 500, nullable = false)
    private String title;

    @Column(name = "CONTENT", columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "MISSION_ID", referencedColumnName = "id",  nullable = false)
    private Mission mission;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "id", nullable = false)
    private Account account;


    @Builder
    public Post(String title, String content, Mission mission, Account account){
        this.title = title;
        this.content = content;
        this.mission = mission;
        this.account = account;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void delete(){
        this.deleteFlag = "Y";
    }

    @Column(name = "DELETE_FLAG")
    @ColumnDefault("'N'")
    private String deleteFlag;

//    @OneToOne
//    @JoinColumn(name = "IMAGE_ID", referencedColumnName = "id", nullable = false)
//    private Image image;
}
