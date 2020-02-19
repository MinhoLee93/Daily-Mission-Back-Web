package com.dailymission.api.springboot.web.repository.common;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class StringToDateTime {
    private int year;
    private int month;
    private int day;

    @Builder
    // date : yyyymmdd
    public StringToDateTime(String date){
        year = Integer.valueOf(date.substring(0,4));
        month = Integer.valueOf(date.substring(4,6));
        day   = Integer.valueOf(date.substring(6,8));
    }

    public LocalDateTime get(){
        return LocalDateTime.of(year,month,day,03,00,00);
    }
}
