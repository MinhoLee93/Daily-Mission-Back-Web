package com.dailymission.api.springboot.web.repository.mission;

import com.dailymission.api.springboot.web.dto.mission.MissionSaveRequestDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * [ 2020-03-11 : 이민호 ]
 * 설명 : MISSION 에 대한 data validation 을 확인한다.
 * */
@Getter
public class MissionValidator {
    private final int MISSION_TITLE_MIN_LENGTH = 1;
    private final int MISSION_TITLE_MAX_LENGTH = 20;
    private final int MISSION_CONTENT_MIN_LENGTH = 10;
    private final int MISSION_CONTENT_MAX_LENGTH = 50;

    @Builder
    public MissionValidator(){

    }


    public boolean checkValidation(MissionSaveRequestDto requestDto){

        // MISSION_TITLE_MIN_LENGTH = 1
        if(requestDto.getTitle().length() < MISSION_TITLE_MIN_LENGTH){
            throw new IllegalArgumentException("미션 타이틀 최소 길이는 " + MISSION_TITLE_MIN_LENGTH + "입니다.");
        }

        // MISSION_TITLE_MAX_LENGTH = 20
        if(requestDto.getTitle().length()> MISSION_TITLE_MAX_LENGTH) {
            throw new IllegalArgumentException("미션 타이틀 최대 길이는 " + MISSION_TITLE_MAX_LENGTH + "입니다.");
        }

        // MISSION_CONTENT_MIN_LENGTH = 10
        if(requestDto.getContent().length() < MISSION_CONTENT_MIN_LENGTH){
            throw new IllegalArgumentException("미션 콘텐츠 최소 길이는 " + MISSION_CONTENT_MIN_LENGTH + "입니다.");
        }

        // MISSION_CONTENT_MAX_LENGTH = 50
        if(requestDto.getContent().length()> MISSION_CONTENT_MAX_LENGTH) {
            throw new IllegalArgumentException("미션 콘텐츠 최대 길이는 " + MISSION_CONTENT_MAX_LENGTH + "입니다.");
        }

        if(requestDto.getFile()==null){
            throw new IllegalArgumentException("미션 생성시 사진은 필수 입니다.");
        }

        String originalFileName = requestDto.getFile().getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        if(!fileExtension .equals(".jpg")
                && !fileExtension.equals(".jpeg")
                && !fileExtension.equals(".gif")
                && !fileExtension.equals(".png")
                && !fileExtension.equals(".bmp")
        ){
            throw new IllegalArgumentException("저장할 수 있는 사진의 확장자는 jpg/jpeg/gif/png/bmp 입니다.");
        }

        LocalDate now =  LocalDate.now();
        if(requestDto.getStartDate().isBefore(now)){
            throw new IllegalArgumentException("미션 시작날짜는 현재보다 빠를수 없습니다.");
        }

        if(requestDto.getStartDate().isAfter(requestDto.getEndDate())){
            throw new IllegalArgumentException("미션 시작날짜는 미션종료날짜보다 느릴수 없습니다.");
        }

        return true;
    }
}
