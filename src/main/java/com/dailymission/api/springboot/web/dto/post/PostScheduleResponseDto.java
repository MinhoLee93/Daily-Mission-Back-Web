package com.dailymission.api.springboot.web.dto.post;

import com.dailymission.api.springboot.web.repository.mission.rule.Week;
import com.dailymission.api.springboot.web.repository.participant.Participant;
import com.dailymission.api.springboot.web.repository.user.User;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class PostScheduleResponseDto implements Serializable {
    private List<DateDto> dates = new ArrayList<>();
    private List<PostHistoryAllDto> histories = new ArrayList<>();

    @Builder
    public PostScheduleResponseDto(LocalDate startDate, Week week, List<PostHistoryDto> histories, List<Participant> participants){

        // fil dates array (7days)
        fillDates(startDate, week);

        // file history
        fillHistories(histories, participants);

    }

    public void fillHistories(List<PostHistoryDto> postHistoryDtoList, List<Participant> participants){
        Map<Long, List<PostHistoryDto>> hash = new HashMap<>();

        /**
         * id : {history, history...}
         * */
        for(PostHistoryDto dto : postHistoryDtoList){
            if(hash.containsKey(dto.getUserId())){
                hash.get(dto.getUserId()).add(dto);
            }else{
                List<PostHistoryDto> temp = new ArrayList<>();
                temp.add(dto);
                hash.put(dto.getUserId(), temp);
            }
        }

        // 전체 참여자
        for(Participant participant : participants){
            User user = participant.getUser();
            PostHistoryAllDto history = PostHistoryAllDto.builder()
                    .userId(user.getId())
                    .userName(user.getName())
                    .thumbnailUrl(user.getThumbnailUrl())
                    .banned(participant.isBanned())
                    .build();

            List<PostHistoryDto> dtoLists = hash.get(user.getId());
            // 제출 기록이 없을 경우 null 일 수 있다.
            // 변경 필요
            if(dtoLists != null){
                for(PostHistoryDto dto : dtoLists){
                    /**
                     * 중복 로직 제거 필요
                     * */
                    if(!history.getDate().contains(dto.getDate())){
                        history.getDate().add(dto.getDate());
                    }
                }
            }


            this.histories.add(history);
        }
    }

    private void fillDates(LocalDate startDate, Week week){

        // 일 ~ 토
        for(int i=0; i<7; i++){
            LocalDate date = startDate.plusDays(i);
            String day = date.getDayOfWeek().toString();
            boolean mandatory = checkMandatory(day, week);


            DateDto dateDto = DateDto.builder()
                                    .date(date)
                                    .day(day)
                                    .mandatory(mandatory)
                                    .build();

            dates.add(dateDto);
        }
    }

    /**
     * 제출해야되는 요일인지 확인
     * */
    private boolean checkMandatory(String day, Week week){

        if(day.equals("SUNDAY")){
            return week.isSun();
        }else if(day.equals("MONDAY")){
            return week.isMon();
        }else if(day.equals("TUESDAY")){
            return week.isTue();
        }else if(day.equals("WEDNESDAY")){
            return week.isWed();
        }else if(day.equals("THURSDAY")){
            return week.isThu();
        }else if(day.equals("FRIDAY")){
            return week.isFri();
        }else if(day.equals("SATURDAY")){
            return week.isSat();
        }

        return false;
    }
}
