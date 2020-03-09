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
 * 미션별 포스트 제출 기록 (전체 참여유저)
 * */
public class PostHistoryDto implements Serializable {
    private Long userId;
    private String userName;
    private String imageUrl;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Builder
    public PostHistoryDto(LocalDateTime datetime, Long userId, String userName, String imageUrl){
        this.userId = userId;
        this.userName = userName;
        this.imageUrl = imageUrl;

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
