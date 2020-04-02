package com.dailymission.api.springboot.web.repository.user;


import com.dailymission.api.springboot.web.repository.common.BaseTimeEntity;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.participant.Participant;
import com.dailymission.api.springboot.web.repository.post.Post;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/*
* Let’s now create the Entity classes of our application.
* Following is the definition of the User class -
* */
@Getter
@Setter
@Entity
//@Table(name = "user", uniqueConstraints = {
//        @UniqueConstraint(columnNames = "email")
//})
@Table(name = "user")
@NoArgsConstructor
public class User extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , length = 20)
    @Size(min = 1, max = 20)
    private String name;

    @Email
    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 2000)
    private String imageUrl;

    @Column(nullable = false, length = 2000)
    private String thumbnailUrl;

    @Column(nullable = false, length = 2000)
    private String thumbnailUrlUserInfo;

    @Column(name = "ORIGINAL_FILE_NAME")
    private String originalFileName;

    @Column(name = "FILE_EXTENSION")
    private String fileExtension;

    @Column(nullable = false)
    private Boolean emailVerified = false;

    @JsonIgnore
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Mission> missions = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Participant> participants = new ArrayList<>();

    // 테스트용 빌더
    @Builder
    public User(String name, String email, String imageUrl, AuthProvider provider, String providerId){
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.provider = provider;
        this.providerId = providerId;
    }

    public boolean isValidUpdateName(String name){

        // null name
        if(name==null){
            return false;
        }

        // check same name
        if(this.name.equals(name)){
            return false;
        }

        return true;
    }

    // 이름 업데이트
    public void updateName(String name){
        this.name = name;
    }


   // 이미지 업데이트
    public void updateImage(String imageUrl){
        // 이미지
        this.imageUrl = imageUrl;
        // 썸네일 -> 재생성
        this.thumbnailUrl = imageUrl;
        this.thumbnailUrlUserInfo = imageUrl;
    }

    // 썸네일(Profile) 업데이트
    public void updateThumbnail(String thumbnailUrl){
        this.thumbnailUrl = thumbnailUrl;
    }

    // 썸네일(UserInfo) 업데이트
    public void updateThumbnailUserInfo(String thumbnailUrlUserInfo){
        this.thumbnailUrlUserInfo = thumbnailUrlUserInfo;
    }
}
