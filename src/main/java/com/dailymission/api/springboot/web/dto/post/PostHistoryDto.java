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
/**
 * [ 2020-03-13 : 이민호 ]
 * 설명 : 유저 정보  + 제출 날짜 리스트
 *        date List 에 제출 일자 목록을 담는다.
 *        ex) {'2020-03-11', '2020-03-12' ..}
 * */
public class PostHistoryDto implements Serializable {
    private Long userId;
    private String userName;
    private String thumbnailUrl;
    private Boolean banned;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private List<LocalDate> date = new ArrayList<>();

    @Builder
    public PostHistoryDto(Long userId, String userName, String thumbnailUrl, Boolean banned){
        this.userId = userId;
        this.userName = userName;
        this.thumbnailUrl = thumbnailUrl;
        this.banned = banned;
    }
}
