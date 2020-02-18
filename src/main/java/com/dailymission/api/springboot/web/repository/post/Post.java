package com.dailymission.api.springboot.web.repository.post;

import com.dailymission.api.springboot.web.repository.common.BaseTimeEntity;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name="IMAGE_URL", nullable = false)
    private String imageUrl;

    @Column(name="THUMBNAIL_URL", nullable = false)
    private String thumbnailUrl;

    @Column(name = "DELETED")
    private boolean deleted;

    @Builder
    public Post(Mission mission, User user, String title, String content
                , String imageUrl){
        this.mission = mission;
        this.user = user;
        this.title = title;
        this.content = content;

        // 이미지
        this.imageUrl = imageUrl;

        // 썸네일
        this.thumbnailUrl = imageUrl;

        this.deleted = false;
    }

    // 내용 업데이트
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 이미지 업데이트
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


    // 삭제
    public void delete(User user){
        if(this.user.getId() != user.getId()){
            throw new IllegalArgumentException("허용되지 않은 유저입니다.");
        }

        if(this.deleted){
            throw new IllegalArgumentException("이미 삭제된 게시글입니다.");
        }

        this.deleted = true;
    }
}
