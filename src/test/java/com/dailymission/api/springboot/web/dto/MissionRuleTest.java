package com.dailymission.api.springboot.web.dto;

import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import com.dailymission.api.springboot.web.repository.mission.rule.Week;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.within;


public class MissionRuleTest {

    private Week givenWeek;

    @Before
    public void setup() {
        givenWeek = Week.builder()
                .sun("Y")
                .mon("Y")
                .tue("Y")
                .wed("Y")
                .thu("Y")
                .fri("N")
                .sat("N").build();
    }

    @Test
    public void  생성후_내부값_동일한지_확인(){
        // when
        MissionRule missionRule = MissionRule.builder().week(givenWeek).build();

        // then
        Week checkWeek = missionRule.getWeek();
        assertThat(checkWeek.getSun()).isEqualTo("Y");
        assertThat(checkWeek.getMon()).isEqualTo("Y");
        assertThat(checkWeek.getTue()).isEqualTo("Y");
        assertThat(checkWeek.getWed()).isEqualTo("Y");
        assertThat(checkWeek.getThu()).isEqualTo("Y");
        assertThat(checkWeek.getFri()).isEqualTo("N");
        assertThat(checkWeek.getSat()).isEqualTo("N");
    }

    @Test
    public void 수정후_변경된값_반영되어있는지(){
        //given
        MissionRule missionRule = new MissionRule(givenWeek);

        //when
        Week week = new Week("Y","Y", "Y", "Y", "Y","Y","Y");
        missionRule.update(week);

        // then
        Week updatedWeek = missionRule.getWeek();
        assertThat(updatedWeek.getSun()).isEqualTo("Y");
        assertThat(updatedWeek.getMon()).isEqualTo("Y");
        assertThat(updatedWeek.getTue()).isEqualTo("Y");
        assertThat(updatedWeek.getWed()).isEqualTo("Y");
        assertThat(updatedWeek.getThu()).isEqualTo("Y");
        assertThat(updatedWeek.getFri()).isEqualTo("Y");
        assertThat(updatedWeek.getSat()).isEqualTo("Y");
    }

    @Test(expected = IllegalArgumentException.class)
    public void save할때_Week이_Null일_경우_llegalArgumentException_에러를_반환하는지(){
        //given
        givenWeek = null;

        // when
        MissionRule missionRule = new MissionRule(givenWeek);
    }

    @Test(expected = IllegalArgumentException.class)
    public void update할때_Week이_Null일_경우_llegalArgumentException_에러를_반환하는지(){
        // given
        MissionRule missionRule = new MissionRule(givenWeek);

        // when
        givenWeek = null;
        missionRule.update(givenWeek);
    }

    @Test(expected = IllegalArgumentException.class)
    public void save할때_Week_내부값에_Y_N_이외의_값이_들어왔을때_llegalArgumentException_에러를_반환하는지(){
        // given
        givenWeek.update("X", "Y", "Y", "Y", "Y", "Y", "Y");

        // when
        MissionRule missionRule = new MissionRule(givenWeek);
    }

    @Test(expected = IllegalArgumentException.class)
    public void update할때_Week_내부값에_Y_N_이외의_값이_들어왔을때_llegalArgumentException_에러를_반환하는지(){
        // given
        MissionRule missionRule = new MissionRule(givenWeek);

        // when
        givenWeek.update("X", "Y", "Y", "Y", "Y", "Y", "Y");
        missionRule.update(givenWeek);

    }


}
