package com.dailymission.api.springboot.web.domain.post;

import com.dailymission.api.springboot.web.domain.common.BaseTimeEntity;
import com.dailymission.api.springboot.web.domain.image.Image;
import com.dailymission.api.springboot.web.domain.mission.Mission;
import com.dailymission.api.springboot.web.domain.user.User;
import edu.umd.cs.findbugs.annotations.CleanupObligation;
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

    @Column(name = "TITLE", length = 500, nullable = false)
    private String title;

    @Column(name = "CONTENT", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "DELETE_FLAG")
    @ColumnDefault("'N'")
    private String deleteFlag;

    @Builder
    public Post(String title, String content, User user){
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }


    @ManyToOne
    @JoinColumn(name = "MISSION_ID", referencedColumnName = "id",  nullable = false)
    private Mission mission;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "id", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "IMAGE_ID", referencedColumnName = "id", nullable = false)
    private Image image;
}
