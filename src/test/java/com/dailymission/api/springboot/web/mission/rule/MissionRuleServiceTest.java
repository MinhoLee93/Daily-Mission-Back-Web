package com.dailymission.api.springboot.web.mission.rule;

import com.dailymission.api.springboot.web.dto.mission.rule.MissionRuleUpdateRequestDto;
import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import com.dailymission.api.springboot.web.repository.mission.rule.MissionRuleRepository;
import com.dailymission.api.springboot.web.repository.mission.rule.Week;
import com.dailymission.api.springboot.web.service.mission.rule.MissionRuleService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@RunWith(MockitoJUnitRunner.class)
public class MissionRuleServiceTest {

    @InjectMocks
    private MissionRuleService missionRuleService;

    @Mock
    private MissionRuleRepository missionRuleRepository;
    private MissionRule missionRule;

    @Before
    public void setup() throws Exception {
        Week week =  Week.builder()
                .sun(true)
                .mon(true)
                .tue(true)
                .wed(true)
                .thu(true)
                .fri(false)
                .sat(false).build();

        missionRule = MissionRule.builder().week(week).build();
    }

    @Test
    public void MissionRule_존재할경우_업데이트_성공(){
        // given
        Week update =  Week.builder()
                .sun(true)
                .mon(true)
                .tue(true)
                .wed(true)
                .thu(true)
                .fri(true)
                .sat(true).build();


        Long id = 1L;
        MissionRuleUpdateRequestDto requestDto = MissionRuleUpdateRequestDto
                .builder()
                .week(update).build();

        given(missionRuleRepository.findById(any())).willReturn(java.util.Optional.ofNullable(missionRule));

        // when
        final Long updatedId = missionRuleService.update(id, requestDto);

        // then
        assertThat(missionRule.getWeek().isSun()).isTrue();
        assertThat(missionRule.getWeek().isMon()).isTrue();
        assertThat(missionRule.getWeek().isTue()).isTrue();
        assertThat(missionRule.getWeek().isWed()).isTrue();
        assertThat(missionRule.getWeek().isThu()).isTrue();
        assertThat(missionRule.getWeek().isFri()).isTrue();
        assertThat(missionRule.getWeek().isSat()).isTrue();
    }

    @Test(expected = NoSuchElementException.class)
    public void MissionRule_존재하지않을경우_업데이트_실패(){
        // given
        Week update =  Week.builder()
                .sun(true)
                .mon(true)
                .tue(true)
                .wed(true)
                .thu(true)
                .fri(true)
                .sat(true).build();


        Long id = 1L;
        MissionRuleUpdateRequestDto requestDto = MissionRuleUpdateRequestDto
                .builder()
                .week(update).build();

        given(missionRuleRepository.findById(any())).willReturn(null);

        // when
        missionRuleService.update(id, requestDto);
    }
}