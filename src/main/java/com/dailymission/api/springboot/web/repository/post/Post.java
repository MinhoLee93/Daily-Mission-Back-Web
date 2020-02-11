package com.dailymission.api.springboot.web.repository.post;

import com.dailymission.api.springboot.web.repository.user.User;
import com.dailymission.api.springboot.web.repository.common.BaseTimeEntity;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;


@Getter
@NoArgsConstructor
@Entity
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MISSION_ID", referencedColumnName = "id",  nullable = false)
    private Mission mission;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "TITLE", length = 500, nullable = false)
    private String title;

    @Column(name = "CONTENT", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name="IMAGE_PATH", nullable = false)
    private String imagePath;

    @Column(name = "DELETE_FLAG")
    @ColumnDefault("'N'")
    private String deleteFlag;

    @Builder
    public Post(Mission mission, User user, String title, String content){
        this.mission = mission;
        this.user = user;
        this.title = title;
        this.content = content;

        // s3
        this.imagePath = "https://s3.ap-northeast-2.amazonaws.com/image.daily-mission.com/default/daily-mission.png";

        this.deleteFlag = "N";
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void updateImage(String imagePath){
        this.imagePath = imagePath;
    }

    public void delete(){
        this.deleteFlag = "Y";
    }

}
