package com.dailymission.api.springboot.web.dto.post;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
public class DateDto  implements Serializable {
    private LocalDate date;
    private String day;

    @Builder
    public DateDto(LocalDate date){
        this.date = date;
        this.day = date.getDayOfWeek().toString();
    }
}
