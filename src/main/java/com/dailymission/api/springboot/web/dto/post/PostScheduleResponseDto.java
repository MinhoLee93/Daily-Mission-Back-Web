package com.dailymission.api.springboot.web.dto.post;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PostScheduleResponseDto implements Serializable {
    private List<DateDto> weekDates = new ArrayList<>();
    private List<PostHistoryDto> histories = new ArrayList<>();

    @Builder
    public PostScheduleResponseDto(List<DateDto> weekDates, List<PostHistoryDto> histories){

        // weekly dates (7 days)
        this.weekDates = weekDates;

        // weekly submit history lists by all participants
        this.histories = histories;

    }
}
