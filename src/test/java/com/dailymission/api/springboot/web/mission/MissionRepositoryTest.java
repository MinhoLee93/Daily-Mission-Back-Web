package com.dailymission.api.springboot.web.mission;

import com.dailymission.api.springboot.web.repository.account.Account;
import com.dailymission.api.springboot.web.repository.account.AccountRepository;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.MissionRepository;
import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import com.dailymission.api.springboot.web.repository.mission.rule.MissionRuleRepository;
import com.dailymission.api.springboot.web.repository.mission.rule.Week;
import com.google.gson.Gson;
import net.bytebuddy.asm.Advice;
import org.hibernate.boot.archive.scan.spi.PackageInfoArchiveEntryHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MissionRepositoryTest {

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MissionRuleRepository missionRuleRepository;

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
//        String jsonAccount = "{\n" +
//                "  \"id\" : 1,\n" +
//                "  \"created_date\" : \"2020-02-07 09:07:34.703000\",\n" +
//                "  \"modified_date\" : \"2020-02-07 09:07:34.703000\",\n" +
//                "  \"delete_flag\" : \"N\",\n" +
//                "  \"email\" : \"test\",\n" +
//                "  \"name\" : \"test\",\n" +
//                "  \"picture\" : \"https://s3.ap-northeast-2.amazonaws.com/daily-mission.com/prototype/%EC%9D%B4%EB%AF%BC%ED%98%B8.jpg\",\n" +
//                "  \"role\" : \"USER\"\n" +
//                "}";
//
//        Account account = new Gson().fromJson(jsonAccount, Account.class);

        Account account = Account.builder()
                                 .name("test")
                                 .email("test")
                                 .picture("test")
                                 .build();

        Account saved = accountRepository.save(account);

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
                .account(saved)
                .build();

    }

    @Test
    public void mission_save_할때_missionRule도_함께_생성이되는지(){
        // when
        Mission saved = missionRepository.save(mission);

        // then
        assertThat(saved.getMissionRule()).isNotNull();
        assertThat(saved.getMissionRule().getWeek()).isNotNull();
    }

    @Test
    public void mission_delete_할때_missionRule도_함께_제거되는지(){
        // when
        Mission saved = missionRepository.save(mission);
        missionRepository.delete(saved);

        boolean existMission = missionRepository.existsById(saved.getId());
        boolean existMissionRule = missionRuleRepository.existsById(saved.getMissionRule().getId());

        // then
        assertThat(existMission).isFalse();
        assertThat(existMissionRule).isFalse();
    }
}
