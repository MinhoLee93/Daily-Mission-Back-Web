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


/**
 * [ 2020-03-18 : 이민호 ]
 * - 테스트 진행시 중요 관점이 아닌 것들은 Mocking 처리해서 외부 의존성들을 줄일 수 있습니다.
 * - 예를 들어 주문 할인 로직이 제대로 동작하는지에 대한 테스트만 진행하지 이게 실제로 데이터베이스에 insert 되는지는 해당 테스트의 관심사가 아닙니다.
 * - 주로 Service 영역을 테스트 합니다.
 * */
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