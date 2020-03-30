package com.dailymission.api.springboot.web.dto.mission;

import com.dailymission.api.springboot.web.repository.mission.Mission;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class MissionPostSubmitCheckDto implements Serializable {
    private Long id;
    private String title;
    private String thumbnailUrl;
    private boolean banned;
    private boolean ended;
    // 금일 제출했는지? (새벽 3시 기준)
    private boolean submit;

    @Builder
    public MissionPostSubmitCheckDto(Mission entity, Boolean banned, Boolean submit){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.thumbnailUrl = entity.getThumbnailUrlAll();
        this.ended = entity.isEnded();
        this.banned = banned;
        this.submit = submit;
    }
}
