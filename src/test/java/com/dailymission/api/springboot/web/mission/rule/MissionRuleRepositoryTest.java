package com.dailymission.api.springboot.web.mission.rule;

import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import com.dailymission.api.springboot.web.repository.mission.rule.MissionRuleRepository;
import com.dailymission.api.springboot.web.repository.mission.rule.Week;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class MissionRuleRepositoryTest {

    @Autowired
    private MissionRuleRepository missionRuleRepository;

    private Week week;

    private MissionRule missionRule;

    @Before
    public void setup(){
        week =  Week.builder()
                        .sun(true)
                        .mon(true)
                        .tue(true)
                        .wed(true)
                        .thu(true)
                        .fri(false)
                        .sat(false).build();

        missionRule = missionRuleRepository.save(MissionRule.builder().week(week).build());
    }

    @Test
    public void existById_존재하는_경우_true(){
        final boolean existById = missionRuleRepository.existsById(missionRule.getId());
        assertThat(existById).isTrue();
    }

    @Test
    public void existById_존재하지_않는_경우_false(){
        final boolean existById = missionRuleRepository.existsById(1111L);
        assertThat(existById).isFalse();
    }

}
