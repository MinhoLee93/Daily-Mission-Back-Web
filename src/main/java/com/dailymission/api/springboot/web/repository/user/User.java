package com.dailymission.api.springboot.web.repository.user;


import com.dailymission.api.springboot.web.repository.common.BaseTimeEntity;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {

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
    public User(String name, String email, String picture, Role role){
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    public User update(String name, String picture){
        this.name = name;
        this.picture = picture;

        return this;
    }

    public String getRoleKey(){

        return this.role.getKey();
    }

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Mission> missions = new ArrayList<>();
}
