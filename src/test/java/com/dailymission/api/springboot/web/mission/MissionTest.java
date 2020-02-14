package com.dailymission.api.springboot.web.mission;

import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import com.dailymission.api.springboot.web.repository.mission.rule.Week;
import com.dailymission.api.springboot.web.repository.user.User;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class MissionTest {

    private Mission mission;

    @Before
    public void setup(){
        // week
        Week week = Week.builder()
                        .sun(true)
                        .mon(true)
                        .tue(true)
                        .wed(true)
                        .thu(true)
                        .fri(false)
                        .sat(false).build();

        MissionRule missionRule = MissionRule.builder().week(week).build();

        // user
        String jsonAccount = "{\n" +
                "  \"id\" : 1,\n" +
                "  \"created_date\" : \"2020-02-07 09:07:34.703000\",\n" +
                "  \"modified_date\" : \"2020-02-07 09:07:34.703000\",\n" +
                "  \"delete_flag\" : \"N\",\n" +
                "  \"email\" : \"test\",\n" +
                "  \"name\" : \"test\",\n" +
                "  \"picture\" : \"https://s3.ap-northeast-2.amazonaws.com/daily-mission.com/prototype/%EC%9D%B4%EB%AF%BC%ED%98%B8.jpg\",\n" +
                "  \"role\" : \"USER\"\n" +
                "}";

        User user = new Gson().fromJson(jsonAccount, User.class);

        // date
        LocalDate startDate = LocalDate.of(2020,01,01);
        LocalDate endDate = LocalDate.of(2020,03,31);

        // mission
        mission = Mission.builder()
                         .title("test")
                         .content("test")
                         .startDate(startDate)
                         .endDate(endDate)
                         .missionRule(missionRule)
                         .user(user)
                         .build();

    }

    @Test
    public void 생성후_내부값_동일한지(){
        // when
        User user = mission.getUser();
        Week week = mission.getMissionRule().getWeek();

        // then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(week.isThu()).isTrue();
        assertThat(week.isFri()).isFalse();
    }

    @Test
    public void 생성후_credential_값이_null_아닌지(){
        // when
        String credential = mission.getCredential();

        // then
        assertThat(credential).isNotNull();
    }
}
