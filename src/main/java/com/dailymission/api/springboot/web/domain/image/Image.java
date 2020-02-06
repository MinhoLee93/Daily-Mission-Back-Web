package com.dailymission.api.springboot.web.domain.image;

import com.dailymission.api.springboot.web.domain.common.BaseTimeEntity;
import com.dailymission.api.springboot.web.domain.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Image extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TYPE", nullable = false)
    private String type;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "SAVE_NAME", nullable = false)
    private String saveName;

    @Column(name = "SIZE", nullable = false)
    private Long size;

    @Column(name = "PATH", nullable = false)
    private String path;

    @Column(name = "DELETE_FLAG", nullable = false)
    @ColumnDefault("'N'")
    private String deleteFlag;

    @Builder
    public Image(String type, String name, String saveName, Long size, String path){
        this.type = type;
        this.name = name;
        this.saveName = saveName;
        this.size = size;
        this.path = path;

    }

    @OneToOne(mappedBy = "image")
    private Post post;
}
