package com.dailymission.api.springboot.batch;

import com.dailymission.api.springboot.web.repository.common.StringToDateTime;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.rule.Week;
import com.dailymission.api.springboot.web.repository.participant.Participant;
import com.dailymission.api.springboot.web.repository.post.Post;
import com.dailymission.api.springboot.web.repository.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class BatchJobConfig {

    /**
     * Web hook Test 7
     * */
    public final String JOB_NAME = "banJob";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private int chunkSize = 100;

    /**
     * [ 2020-03-16 : 이민호 ]
     * 설명 : 미션 인증 요일에 인증하지 않은 사용자를 강퇴한다.
     *       만약 미션의 endDate 가 지났을 경우 해당 미션을 종료한다.
     * */
    @Bean
    public Job job(){
        return jobBuilderFactory.get(JOB_NAME)
                                .start(step())
                                .build();
    }

    @Bean
    @JobScope
    public Step step(){
        return stepBuilderFactory.get("jpaPagingStep")
                .<Participant, Participant>chunk(chunkSize)
                .reader(missionReader())
                .processor(banProcessor(null))
                .writer(bannedParticipantWriter())
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Participant> missionReader(){

        // 페이지 고정
        JpaPagingItemReader<Participant> reader = new JpaPagingItemReader<Participant>(){
              @Override
              public int getPage(){
                  return 0;
              }
        };
        reader.setName("jpaPagingItemReader");
        reader.setQueryString("SELECT p FROM Participant p where banned = false order by id");
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setPageSize(chunkSize);

        return reader;
    }

    // List<Participant>
    @Bean
    @StepScope
    public ItemProcessor<Participant, Participant> banProcessor(@Value("#{jobParameters[requestDate]}") String requestDate){
        return participant -> {
            // 03:00
            LocalDateTime endTime = StringToDateTime.builder().date(requestDate).build().get();
            LocalDateTime startTime = endTime.minusDays(1);

            // 요일
            DayOfWeek week = startTime.getDayOfWeek();
            log.info(">>>>>>>>>>>>>>  current date {}", endTime.toString());
            log.info(">>>>>>>>>>>>>>  current day of week {}", week.getDisplayName(TextStyle.FULL, Locale.KOREAN));

            // Entity
            User user = participant.getUser();
            Mission mission = participant.getMission();
            Week rule = mission.getMissionRule().getWeek();
            log.info(">>>>>>>>>>>>>>  current mission {}", mission.getTitle());
            log.info(">>>>>>>>>>>>>>  user {}", user.getName());

            // 제출안해도 되는 요일이면 pass
            switch (week.getValue()){
                case 1:
                    if(!rule.isMon()){
                        log.info("Monday is not mandatory");
                        return null;
                    }
                    break;
                case 2:
                    if(!rule.isTue()){
                        log.info("Tuesday is not mandatory");
                        return null;
                    }
                    break;
                case 3:
                    if(!rule.isWed()){
                        log.info("Wednesday is not mandatory");
                        return null;
                    }
                    break;
                case 4:
                    if(!rule.isThu()){
                        log.info("Thursday is not mandatory");
                        return null;
                    }
                    break;
                case 5:
                    if(!rule.isFri()){
                        log.info("friday is not mandatory");
                        return null;
                    }
                    break;
                case 6:
                    if(!rule.isSat()){
                        log.info("saturday is not mandatory");
                        return null;
                    }
                    break;
                case 7:
                    if(!rule.isSun()){
                        log.info("sunday is not mandatory");
                        return null;
                    }
                    break;
            }


            // 제출한 포스트 목록 (전날 03:00 ~ 현재 03:00)
            List<Post> posts = mission.getPosts()
                    .stream()
                    .filter(post -> (post.getCreatedDate().isBefore(endTime))&&(post.getCreatedDate().isAfter(startTime)))
                    .filter(post -> post.getUser().equals(user))
                    .collect(Collectors.toList());

            // 포스트 제출 유저는 미션 성공
            if(posts.size()>0){
                for(Post p : posts){
                    log.info(">>>>>>>>>>>>>> 포스트 {}", p.getTitle());
                }
            }else{
                // 미 제출자 강퇴 처리
                log.info(">>>>>>>>>>>>>> {} 강퇴", user.getName());
                participant.ban();
            }

            return participant;
        };
    }

    @Bean
    @StepScope
    public JpaItemWriter<Participant> bannedParticipantWriter(){
        JpaItemWriter<Participant> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);

        return writer;
    }
}
