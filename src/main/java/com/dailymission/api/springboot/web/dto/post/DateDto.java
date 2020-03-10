package com.dailymission.api.springboot.web.dto.post;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
public class DateDto  implements Serializable {
    private LocalDate date;
    private String day;
    private boolean mandatory;

    @Builder
    public DateDto(LocalDate date, String day, boolean mandatory){
        this.date = date;
        this.day = day;
        this.mandatory =  mandatory;
    }
}
