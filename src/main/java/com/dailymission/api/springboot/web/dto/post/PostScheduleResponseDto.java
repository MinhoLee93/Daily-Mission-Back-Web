package com.dailymission.api.springboot.web.dto.post;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Getter
public class PostScheduleResponseDto implements Serializable {
    private List<DateDto> dates = new ArrayList<>();
    private List<PostHistoryAllDto> histories = new ArrayList<>();

    @Builder
    public PostScheduleResponseDto(LocalDate startDate, List<PostHistoryDto> histories){

        // fil dates array (7days)
        fillDates(startDate);

        // file history
        fillHistories(histories);

    }

    public void fillHistories(List<PostHistoryDto> postHistoryDtoList){
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

        Iterator<Long> iter = hash.keySet().iterator();
        while(iter.hasNext()){
            // id
            Long id = iter.next();
            // object
            PostHistoryAllDto history = null;
            // dtos
            List<PostHistoryDto> dtos = hash.get(id);

            /**
             *  {
             *     userId: 2,
             *     userName: 'seowon lee',
             *     banned: false,
             *     submitDay: ['2020-03-07', '2020-03-06', '2020-03-05'],
             *   }
             * */
            // get date
            for(int i= 0; i< dtos.size(); i++){
                PostHistoryDto dto = dtos.get(i);
                if(i==0){
                    history = PostHistoryAllDto.builder()
                            .userId(dto.getUserId())
                            .userName(dto.getUserName())
                            .imageUrl(dto.getImageUrl())
                            .build();
                }
                history.getDate().add(dto.getDate());
            }

            this.histories.add(history);
        }
    }

    private void fillDates(LocalDate startDate){

        // 일 ~ 토
        for(int i=0; i<7; i++){
            LocalDate week = startDate.plusDays(i);
            DateDto date = DateDto.builder()
                                    .date(week)
                                    .build();

            dates.add(date);
        }
    }
}
