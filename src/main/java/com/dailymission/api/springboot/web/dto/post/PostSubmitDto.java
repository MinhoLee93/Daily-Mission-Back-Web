package com.dailymission.api.springboot.web.dto.post;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
/**
 * [ 2020-03-13 : 이민호 ]
 * 설명 : 포스트 제출 기록
 * */
public class PostSubmitDto implements Serializable {
    private Long userId;
    private String userName;
    private String thumbnailUrl;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Builder
    public PostSubmitDto(LocalDateTime datetime, Long userId, String userName, String thumbnailUrl){
        this.userId = userId;
        this.userName = userName;
        this.thumbnailUrl = thumbnailUrl;

        // 새벽 3시 이전제출은 이전날 제출로 인식
        LocalDateTime check = LocalDateTime.of(datetime.getYear(),datetime.getMonth(),datetime.getDayOfMonth(), 3,0,0);
        if(datetime.isBefore(check)){
            // 하루전 으로 변경
            date = datetime.minusDays(1).toLocalDate();
        }else{
            date = datetime.toLocalDate();
        }
    }
}
