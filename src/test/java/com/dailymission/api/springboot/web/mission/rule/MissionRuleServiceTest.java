package com.dailymission.api.springboot.web.mission.rule;

import com.dailymission.api.springboot.web.dto.missionRule.MissionRuleUpdateRequestDto;
import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import com.dailymission.api.springboot.web.repository.mission.rule.MissionRuleRepository;
import com.dailymission.api.springboot.web.repository.mission.rule.Week;
import com.dailymission.api.springboot.web.service.MissionRuleService;
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
                         .sun("Y")
                         .mon("Y")
                         .tue("Y")
                         .wed("Y")
                         .thu("Y")
                         .fri("N")
                         .sat("N").build();

        missionRule = MissionRule.builder().week(week).build();
    }

    @Test
    public void MissionRule_존재할경우_업데이트_성공(){
        // given
        Week update =  Week.builder()
                .sun("Y")
                .mon("Y")
                .tue("Y")
                .wed("Y")
                .thu("Y")
                .fri("Y")
                .sat("Y").build();


        Long id = 1L;
        MissionRuleUpdateRequestDto requestDto = MissionRuleUpdateRequestDto
                                                .builder()
                                                .week(update).build();

        given(missionRuleRepository.findById(any())).willReturn(java.util.Optional.ofNullable(missionRule));

        // when
        final Long updatedId = missionRuleService.update(id, requestDto);

        // then
        assertThat(missionRule.getWeek().getSun()).isEqualTo("Y");
        assertThat(missionRule.getWeek().getMon()).isEqualTo("Y");
        assertThat(missionRule.getWeek().getTue()).isEqualTo("Y");
        assertThat(missionRule.getWeek().getWed()).isEqualTo("Y");
        assertThat(missionRule.getWeek().getThu()).isEqualTo("Y");
        assertThat(missionRule.getWeek().getFri()).isEqualTo("Y");
        assertThat(missionRule.getWeek().getSat()).isEqualTo("Y");
    }

    @Test(expected = NoSuchElementException.class)
    public void MissionRule_존재하지않을경우_업데이트_실패(){
        // given
        Week update =  Week.builder()
                .sun("Y")
                .mon("Y")
                .tue("Y")
                .wed("Y")
                .thu("Y")
                .fri("Y")
                .sat("Y").build();


        Long id = 1L;
        MissionRuleUpdateRequestDto requestDto = MissionRuleUpdateRequestDto
                .builder()
                .week(update).build();

        given(missionRuleRepository.findById(any())).willReturn(null);

        // when
        missionRuleService.update(id, requestDto);
    }
}
