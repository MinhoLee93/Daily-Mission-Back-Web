package com.dailymission.api.springboot.web.dto.post;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class PostHistoryAllDto  implements Serializable {
    private Long userId;
    private String userName;
    private String imageUrl;
    private Boolean banned;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private List<LocalDate> date = new ArrayList<>();

    @Builder
    public PostHistoryAllDto(Long userId, String userName, String imageUrl, Boolean banned){
        this.userId = userId;
        this.userName = userName;
        this.imageUrl = imageUrl;
        this.banned = banned;
    }
}
