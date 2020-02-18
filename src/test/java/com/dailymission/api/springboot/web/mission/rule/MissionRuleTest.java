package com.dailymission.api.springboot.web.mission.rule;

import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import com.dailymission.api.springboot.web.repository.mission.rule.Week;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class MissionRuleTest {

    private Week givenWeek;

    @Before
    public void setup() {
        givenWeek = Week.builder()
                .sun(true)
                .mon(true)
                .tue(true)
                .wed(true)
                .thu(true)
                .fri(false)
                .sat(false).build();
    }

    @Test
    public void  생성후_내부값_동일한지_확인(){
        // when
        MissionRule missionRule = MissionRule.builder().week(givenWeek).build();

        // then
        Week checkWeek = missionRule.getWeek();
        assertThat(checkWeek.isSun()).isTrue();
        assertThat(checkWeek.isMon()).isTrue();
        assertThat(checkWeek.isTue()).isTrue();
        assertThat(checkWeek.isWed()).isTrue();
        assertThat(checkWeek.isThu()).isTrue();
        assertThat(checkWeek.isFri()).isFalse();
        assertThat(checkWeek.isSat()).isFalse();

        System.out.println(new Gson().toJson(missionRule).toString());
    }

    @Test
    public void 수정후_변경된값_반영되어있는지(){
        //given
        MissionRule missionRule = new MissionRule(givenWeek);

        //when
        Week week = new Week(true,true, true, true, true,true,true);
        missionRule.update(week);

        // then
        Week updatedWeek = missionRule.getWeek();
        assertThat(updatedWeek.isSun()).isTrue();
        assertThat(updatedWeek.isMon()).isTrue();
        assertThat(updatedWeek.isTue()).isTrue();
        assertThat(updatedWeek.isWed()).isTrue();
        assertThat(updatedWeek.isThu()).isTrue();
        assertThat(updatedWeek.isFri()).isTrue();
        assertThat(updatedWeek.isSat()).isTrue();
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

    // String -> Boolean으로 변경되면서 더이상 테스트할 필요가 없어졌다.
    @Test(expected = IllegalArgumentException.class)
    @Ignore
    public void save할때_Week_내부값에_Y_N_이외의_값이_들어왔을때_llegalArgumentException_에러를_반환하는지(){
        // given
        givenWeek.update(true, true, true, true, true, true, true);

        // when
        MissionRule missionRule = new MissionRule(givenWeek);
    }

    // String -> Boolean으로 변경되면서 더이상 테스트할 필요가 없어졌다.
    @Test(expected = IllegalArgumentException.class)
    @Ignore
    public void update할때_Week_내부값에_Y_N_이외의_값이_들어왔을때_llegalArgumentException_에러를_반환하는지(){
        // given
        MissionRule missionRule = new MissionRule(givenWeek);

        // when
        givenWeek.update(true, true, true, true, true, true, true);
        missionRule.update(givenWeek);

    }


}
