package com.dailymission.api.springboot.web.service.schedule;

import com.dailymission.api.springboot.web.repository.participant.Participant;
import com.dailymission.api.springboot.web.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * POST 제출 history/schedule service
 * */
@RequiredArgsConstructor
@Service
public class ScheduleService {
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public boolean isSubmitToday(Participant participant){
        // result
        boolean isSubmit = false;

        // now
        LocalDateTime start = LocalDateTime.now();

        /**
         * [ 2020-03-11 : 이민호 ]
         * 설명 : 현재시간이 0시 ~ 03시 경우에 전날 03시 ~ 현재시간까지 제출기록이 있는지 확인한다.
         *       현재시간이 03시 ~ 24시 경우에 금일 03시 ~ 현재시간까지 제출기록이 있는지 확인한다.
         * */
        // 0시  ~ 03시
        LocalDateTime criteria = LocalDate.now().atTime(03,00);
        if(start.isBefore(criteria)){
            // 전날 새벽 3시 ~ 현재
            isSubmit =  postRepository.countPostSubmit(participant.getMission().getId()
                                                        ,participant.getUser().getId()
                                                        ,criteria.minusDays(1)
                                                        ,start) > 0;


        }else{
            // 03시 ~ 24시
            // 금일 새벽 3시 ~ 현재
            isSubmit =  postRepository.countPostSubmit(participant.getMission().getId()
                                                        ,participant.getUser().getId()
                                                        ,criteria
                                                        ,start) > 0;
        }

        return isSubmit;
    }
}
