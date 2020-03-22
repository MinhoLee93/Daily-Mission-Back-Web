package com.dailymission.api.springboot.web.mission;

import com.dailymission.api.springboot.web.mission.rule.MissionRuleSetup;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import com.dailymission.api.springboot.web.repository.mission.rule.Week;
import com.dailymission.api.springboot.web.repository.user.User;
import com.dailymission.api.springboot.web.user.UserSetup;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;


/**
 * [ 2020-03-20 : 이민호 ]
 * - 객체지향에서 본인의 책임(기능)은 본인 스스로가 제공해야 합니다.
 * - 특히 엔티티 객체들은 가장 핵심 객체이고 이 객체를 사용하는 계층들이 다양하게 분포되기 때문에 반드시 테스트 코드를 작성해야합니다.
 * - entity, Embeddable 객체 등의 객체들도 반드시 테스트 코드를 작성해야합니다.
 * */
public class MissionTest {

    private final String TITLE = "TITLE";
    private final String CONTENT = "MISSION_CONTENT";
    private final String ORIGINAL_FILE_NAME = "ORIGINAL_FILE_NAME.jpg";
    private final String FILE_EXTENSION = ".jpg";
    private final LocalDate START_DATE = LocalDate.of(2020,04,01);
    private final LocalDate END_DATE = LocalDate.of(2020,04,28);
    private final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    private final String IMAGE_URL = "IMAGE_URL.jpg";
    private final String UPDATE_IMAGE_URL = "UPDATE_IMAGE_URL.jpg";

    private MissionRule missionRule;
    private User user;
    private Mission mission;


    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : Test 에 사용할 MISSION 을 생성한다.
     * */
    @Before
    public void setup(){
        // mission rule
        missionRule = MissionRuleSetup.builder().build().get();

        // user
        user = UserSetup.builder().build().get();

        // mission
        mission = Mission.builder()
                         .user(user)
                         .missionRule(missionRule)
                         .title(TITLE)
                         .content(CONTENT)
                         .originalFileName(ORIGINAL_FILE_NAME)
                         .fileExtension(FILE_EXTENSION)
                         .imageUrl(IMAGE_URL)
                         .startDate(START_DATE)
                         .endDate(END_DATE)
                         .build();
    }

    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : 미션 생성후 생성된 미션의 전체 변수들을 확인한다.
     * */
    @Test
    public void create_mission_THEN_check_all_mission_variables(){

        // then
        assertThat(mission.getUser()).isEqualTo(user);
        assertThat(mission.getMissionRule()).isEqualTo(missionRule);
        assertThat(mission.getTitle()).isEqualTo(TITLE);
        assertThat(mission.getContent()).isEqualTo(CONTENT);
        assertThat(mission.getOriginalFileName()).isEqualTo(ORIGINAL_FILE_NAME);
        assertThat(mission.getFileExtension()).isEqualTo(FILE_EXTENSION);
        assertThat(mission.getStartDate()).isEqualTo(START_DATE);
        assertThat(mission.getEndDate()).isEqualTo(END_DATE);
        assertThat(mission.getImageUrl()).isEqualTo(IMAGE_URL);
        /**
         * [ 2020-03-20 : 이민호 ]
         * 설명 : 미션 생성시 모든 Thumbnail url 은 리사이징 전까지 Image url 과 동일하다.
         * */
        assertThat(mission.getThumbnailUrlNew()).isEqualTo(IMAGE_URL);
        assertThat(mission.getThumbnailUrlAll()).isEqualTo(IMAGE_URL);
        assertThat(mission.getThumbnailUrlHot()).isEqualTo(IMAGE_URL);
        assertThat(mission.getThumbnailUrlDetail()).isEqualTo(IMAGE_URL);
        /**
         * [ 2020-03-20 : 이민호 ]
         * 설명 : 미션 생성시 모든 end/delete 는 false 가 default 값 이다.
         * */
        assertThat(mission.isEnded()).isFalse();
        assertThat(mission.isDeleted()).isFalse();
        /**
         * [ 2020-03-20 : 이민호 ]
         * 설명 : 미션 생성시 default credential 은 null 이다.
         *       이후 setCredential method 를 호출해 credential 값을 임의로 생성해 지정한다.
         * */
        assertThat(mission.getCredential()).isNull();
    }


    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : 미션 생성후 setCredential method 를 호출해 임의의 credential 값을 지정한다.
     *        생성된 credential 값을 matchCredential method 를 사용해 검증한다.
     * */
    @Test
    public void create_mission_and_set_credential_THNE_check_credential_is_not_null(){
        // when
        String credential = mission.setCredential(PASSWORD_ENCODER);

        // then
        assertThat(mission.getCredential()).isNotNull();
        assertThat(mission.matchCredential(credential,PASSWORD_ENCODER)).isTrue();
    }

    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : MISSION 이 END 됬을 경우 isPossibleToParticipant 값이 FALSE 이다.
     * */
    @Test
    public void if_mission_is_ended_THNE_is_Possible_To_Participant_return_FALSE(){
        // when
        mission.end();

        // then
        assertThat(mission.isPossibleToParticipate(LocalDate.now())).isFalse();
    }

    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : MISSION 이 DELETE 됬을 경우 isPossibleToParticipant 값이 FALSE 이다.
     * */
    @Test
    public void if_mission_is_deleted_THNE_is_Possible_To_Participant_return_FALSE(){
        // when
        mission.delete();

        // then
        assertThat(mission.isPossibleToParticipate(LocalDate.now())).isFalse();
    }

    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : MISSION 의 Start_Date 가 지났을 경우
     *        isPossibleToParticipant 값이 FALSE 이다.
     * */
    @Test
    public void if_today_is_after_then_startDate_THNE_is_Possible_To_Participant_return_FALSE(){
        // when
        LocalDate afterStartDate = START_DATE.plusDays(1);
        Boolean result = mission.isPossibleToParticipate(afterStartDate);

        // then
        assertThat(result).isFalse();
    }

    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : MISSION 이 NOT END/DELETE 이고, StartDate 가 지나지 않았을 경우
     *        isPossibleToParticipant 값이 TRUE 이다.
     * */
    @Test
    public void is_Possible_To_Participant_return_TRUE(){
        // when
        LocalDate beforeStartDate = START_DATE.minusDays(1);
        Boolean result = mission.isPossibleToParticipate(beforeStartDate);

        // then
        assertThat(result).isTrue();
    }

    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : IMAGE 를 변경하면
     *        나머지 Thumbnail 도 동일하게 변경된다.
     *        이 값들은 이미지 리사이징 후 업데이트된다.
     * */
    @Test
    public void update_image_THEN_thumbnail_image_urls_changed_also (){
        // when
        mission.updateImage(UPDATE_IMAGE_URL);

        // then
        assertThat(mission.getImageUrl()).isEqualTo(UPDATE_IMAGE_URL);
        assertThat(mission.getThumbnailUrlNew()).isEqualTo(UPDATE_IMAGE_URL);
        assertThat(mission.getThumbnailUrlAll()).isEqualTo(UPDATE_IMAGE_URL);
        assertThat(mission.getThumbnailUrlHot()).isEqualTo(UPDATE_IMAGE_URL);
        assertThat(mission.getThumbnailUrlDetail()).isEqualTo(UPDATE_IMAGE_URL);
    }

    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : THUMBNAIL_NEW 를 변경한다.
     *        이미지와 나머지 썸네일들은 변경되지 않는다.
     * */
    @Test
    public void update_thumbnail_new_THEN_thumbnail_new_is_changed (){
        // when
        mission.updateThumbnailNew(UPDATE_IMAGE_URL);

        // then
        assertThat(mission.getThumbnailUrlNew()).isEqualTo(UPDATE_IMAGE_URL);
        assertThat(mission.getImageUrl()).isEqualTo(IMAGE_URL);
        assertThat(mission.getThumbnailUrlAll()).isEqualTo(IMAGE_URL);
        assertThat(mission.getThumbnailUrlHot()).isEqualTo(IMAGE_URL);
        assertThat(mission.getThumbnailUrlDetail()).isEqualTo(IMAGE_URL);
    }

    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : THUMBNAIL_ALL 를 변경한다.
     *        이미지와 나머지 썸네일들은 변경되지 않는다.
     * */
    @Test
    public void update_thumbnail_all_THEN_thumbnail_new_is_changed (){
        // when
        mission.updateThumbnailAll(UPDATE_IMAGE_URL);

        // then
        assertThat(mission.getThumbnailUrlAll()).isEqualTo(UPDATE_IMAGE_URL);
        assertThat(mission.getImageUrl()).isEqualTo(IMAGE_URL);
        assertThat(mission.getThumbnailUrlNew()).isEqualTo(IMAGE_URL);
        assertThat(mission.getThumbnailUrlHot()).isEqualTo(IMAGE_URL);
        assertThat(mission.getThumbnailUrlDetail()).isEqualTo(IMAGE_URL);
    }

    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : THUMBNAIL_HOT 를 변경한다.
     *        이미지와 나머지 썸네일들은 변경되지 않는다.
     * */
    @Test
    public void update_thumbnail_hot_THEN_thumbnail_new_is_changed (){
        // when
        mission.updateThumbnailHot(UPDATE_IMAGE_URL);

        // then
        assertThat(mission.getThumbnailUrlHot()).isEqualTo(UPDATE_IMAGE_URL);
        assertThat(mission.getImageUrl()).isEqualTo(IMAGE_URL);
        assertThat(mission.getThumbnailUrlNew()).isEqualTo(IMAGE_URL);
        assertThat(mission.getThumbnailUrlAll()).isEqualTo(IMAGE_URL);
        assertThat(mission.getThumbnailUrlDetail()).isEqualTo(IMAGE_URL);
    }

    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : THUMBNAIL_DETAIL 를 변경한다.
     *        이미지와 나머지 썸네일들은 변경되지 않는다.
     * */
    @Test
    public void update_thumbnail_detail_THEN_thumbnail_new_is_changed (){
        // when
        mission.updateThumbnailDetail(UPDATE_IMAGE_URL);

        // then
        assertThat(mission.getThumbnailUrlDetail()).isEqualTo(UPDATE_IMAGE_URL);
        assertThat(mission.getThumbnailUrlHot()).isEqualTo(IMAGE_URL);
        assertThat(mission.getImageUrl()).isEqualTo(IMAGE_URL);
        assertThat(mission.getThumbnailUrlNew()).isEqualTo(IMAGE_URL);
        assertThat(mission.getThumbnailUrlAll()).isEqualTo(IMAGE_URL);
    }

    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : MISSION 을 DELETE 하면 END 값도 TRUE 로 변경된다.
     * */
    @Test
    public void delete_mission_THEN_check_end_is_ended_also (){
        // when
        mission.delete();

        // then
        assertThat(mission.isDeleted()).isTrue();
        assertThat(mission.isEnded()).isTrue();
    }


    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : MISSION 을 END 하면 DELETE 값은 변경되지 않는다.
     * */
    @Test
    public void end_mission_THEN_check_delete_is_not_deleted (){
        // when
        mission.end();

        // then
        assertThat(mission.isEnded()).isTrue();
        assertThat(mission.isDeleted()).isFalse();
    }

    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : MISSION 생성 유저가 아닌 USER 가 isDeletable 을 호출했을 경우
     *       IllegalArgumentException 에러가 발생한다.
     * */
    @Test(expected = IllegalArgumentException.class)
    public void call_deletable_with_illegal_user_THEN_throw_IllegalArgumentException (){
        // given
        User illegalUser = User.builder().build();
        illegalUser.setId(123L);

        // when
        mission.isDeletable(illegalUser);
    }

    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : endDate 가 지나지 않았을 경우
     *        isEndable 메서드가 False 를 return 한다.
     * */
    @Test
    public void if_now_is_not_after_endDate_THEN_isEndable_return_false (){
        // given
        LocalDate now = END_DATE.minusDays(1);
        Mission spyMission = spy(mission);
        doReturn(1).when(spyMission).getParticipantCountNotBanned();

        // when
        boolean result = spyMission.isEndable(now);

        // then
        assertThat(result).isFalse();
    }

    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : endDate 가 지났을경우
     *        isEndable 메서드가 true 를 return 한다.
     * */
    @Test
    public void if_now_is__after_endDate_THEN_isEndable_return_true (){
        // given
        LocalDate now = END_DATE.plusDays(1);

        // when
        boolean result = mission.isEndable(now);

        // then
        assertThat(result).isTrue();
    }

    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : getParticipantCountNotBanned 가 0보다 클 경우
     *        즉, 참여중인 User 가 1명이라도 있을 경우
     *        isEndable 메서드가 False 를 return 한다.
     * */
    @Test
    public void if_participant_count_not_banned_bigger_than_0_THEN_isEndable_return_false (){
        // given
        LocalDate now = END_DATE.minusDays(1);
        Mission spyMission = spy(mission);
        doReturn(1).when(spyMission).getParticipantCountNotBanned();

        // when
        boolean result = spyMission.isEndable(now);

        // then
        assertThat(result).isFalse();
    }

    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : getParticipantCountNotBanned 가 0인 경우
     *        isEndable 메서드가 true 를 return 한다.
     * */
    @Test
    public void if_participant_count_not_banned_is_0_THEN_isEndable_return_true (){
        // given
        LocalDate now = END_DATE.minusDays(1);
        Mission spyMission = spy(mission);
        doReturn(0).when(spyMission).getParticipantCountNotBanned();

        // when
        boolean result = spyMission.isEndable(now);

        // then
        assertThat(result).isTrue();
    }

    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : 제출 의무요일 에 맞는 boolean 값을 return 한다.
     * */
    @Test
    public void check_mandatory_value(){
        // given
        mission.getMissionRule().getWeek().setSun(true);

        // when
        boolean result = mission.checkMandatory("SUNDAY");

        // then
        assertThat(result).isTrue();
    }

    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : startDate 가 now 보다 이전일 경우
     *       IllegalArgumentException 에러가 발생한다.
     * */
    @Test(expected = IllegalArgumentException.class)
    public void if_startDate_is_before_now_THEN_throw_IllegalArgumentException(){
        // given
        LocalDate now = START_DATE.plusDays(1);

        // when
        mission.isValidStartDate(now);
    }

    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : startDate 가 endDate 보다 늦을 경우
     *       IllegalArgumentException 에러가 발생한다.
     * */
    @Test(expected = IllegalArgumentException.class)
    public void if_startDate_is_after_endDate_THEN_throw_IllegalArgumentException(){
        // given
        LocalDate now = START_DATE.minusDays(1);
        LocalDate startDate = END_DATE.plusDays(1);
        mission = Mission.builder()
                         .startDate(startDate)
                         .endDate(END_DATE)
                         .build();

        // when
        mission.isValidStartDate(now);
    }

    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : file extension 이 {".jpg", ".jpeg", ".gif", ".png", ".bmp"} 가 아니라면
     *       IllegalArgumentException 에러가 발생한다.
     * */
    @Test(expected = IllegalArgumentException.class)
    public void if_fileExtension_is_not_valid_THEN_throw_IllegalArgumentException(){
        // given
        mission = Mission.builder()
                         .fileExtension(".jiff")
                         .build();

        // when
        mission.isValidFileExtension();
    }

    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : 모든 week 가 false 라면
     *       IllegalArgumentException 에러가 발생한다.
     * */
    @Test(expected = IllegalArgumentException.class)
    public void if_week_all_false_THEN_thorw_IllegalArgumentException(){
        // given
        final Week week = Week.builder()
                              .sun(false)
                              .mon(false)
                              .tue(false)
                              .wed(false)
                              .thu(false)
                              .fri(false)
                              .sun(false)
                              .build();
        final MissionRule missionRule = MissionRule.builder().week(week).build();

        // when
        missionRule.isValidWeek();
    }

    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : isValidMission 을 통과한다.
     * */
    @Test
    public void is_valid_mission_true(){
        // given
        LocalDate now = START_DATE.minusDays(1);

        // when
        mission.isValidMission(now);
    }
}
