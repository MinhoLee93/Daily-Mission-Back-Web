package com.dailymission.api.springboot.web.participant;

import com.dailymission.api.springboot.web.repository.participant.Participant;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * [ 2020-03-18 : 이민호 ]
 * 설명 :  객체지향에서 본인의 책임(기능)은 본인 스스로가 제공해야 합니다.
 *         특히 엔티티 객체들은 가장 핵심 객체이고
 *         이 객체를 사용하는 계층들이 다양하게 분포되기 때문에
 *         반드시 테스트 코드를 작성해야합니다.
 * */
public class ParticipantTest {

    private Participant participant;

    @Before
    public void setup(){
        // user
        ParticipantSetup participantSetup = ParticipantSetup.builder().mission(null).user(null).build();
        participant = participantSetup.build();
    }

    /**
     * [ 2020-03-18 : 이민호 ]
     * 설명 : PARTICIPANT 생성 후 ban 확인
     * */
    @Test
    public void create_participant_THEN_check_ban_is_false(){
        // then
        assertThat(participant.isBanned()).isFalse();
    }

    /**
     * [ 2020-03-18 : 이민호 ]
     * 설명 : PARTICIPANT ban 후 ban 되었는지 확인
     * */
    @Test
    public void ban_participant_THEN_check_ban_is_true(){

        // when
        participant.ban();

        // then
        assertThat(participant.isBanned()).isTrue();
    }
}
