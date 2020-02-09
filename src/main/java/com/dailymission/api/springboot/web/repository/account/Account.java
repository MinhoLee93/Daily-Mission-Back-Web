package com.dailymission.api.springboot.web.repository.account;


import com.dailymission.api.springboot.web.repository.common.BaseTimeEntity;
import com.dailymission.api.springboot.web.repository.join.JoinMission;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
public class Account extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;

    @Column(name="NAME", nullable = false)
    private String name;

    @Column(name="EMAIL", nullable = false)
    private String email;

    @Column(name="PICTURE")
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(name="ROLE", nullable = false)
    @ColumnDefault("'USER'")
    private Role role;

    @Column(name = "DELETE_FLAG", nullable = false)
    @ColumnDefault("'N'")
    private String deleteFlag;

    @Builder
    public Account(String name, String email, String picture){
        this.name = name;
        this.email = email;
        this.picture = picture;

        this.role = Role.USER;
        this.deleteFlag = "N";
    }

    public Account update(String name, String picture){
        this.name = name;
        this.picture = picture;

        return this;
    }

    public String getRoleKey(){

        return this.role.getKey();
    }

    @OneToMany(mappedBy = "account")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    private List<Mission> missions = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    private List<JoinMission> joinMissions = new ArrayList<>();
}
