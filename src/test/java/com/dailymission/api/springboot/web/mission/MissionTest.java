package com.dailymission.api.springboot.web.mission;

import com.dailymission.api.springboot.web.repository.account.Account;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import com.dailymission.api.springboot.web.repository.mission.rule.Week;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class MissionTest {

    private Mission mission;

    @Before
    public void setup(){
        // week
        Week week = Week.builder()
                        .sun("Y")
                        .mon("Y")
                        .tue("Y")
                        .wed("Y")
                        .thu("Y")
                        .fri("N")
                        .sat("N").build();

        MissionRule missionRule = MissionRule.builder().week(week).build();

        // account
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

        Account account = new Gson().fromJson(jsonAccount, Account.class);

        // date
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date startDate = new Date();
        Date endDate = cal.getTime();

        // mission
        mission = Mission.builder()
                         .title("test")
                         .content("test")
                         .startDate(startDate)
                         .endDate(endDate)
                         .missionRule(missionRule)
                         .account(account)
                         .build();

    }

    @Test
    public void 생성후_내부값_동일한지(){
        // when
        Account account = mission.getAccount();
        Week week = mission.getMissionRule().getWeek();

        // then
        assertThat(account.getId()).isEqualTo(1L);
        assertThat(week.getThu()).isEqualTo("Y");
        assertThat(week.getFri()).isEqualTo("N");
    }

    @Test
    public void 생성후_credential_값이_null_아닌지(){
        // when
        String credential = mission.getCredential();

        // then
        assertThat(credential).isNotNull();
    }
}
