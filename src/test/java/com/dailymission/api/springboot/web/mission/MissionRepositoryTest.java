package com.dailymission.api.springboot.web.mission;

import com.dailymission.api.springboot.config.JpaConfig;
import com.dailymission.api.springboot.config.QueryDslConfig;
import com.dailymission.api.springboot.web.mission.rule.MissionRuleSetup;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.MissionRepository;
import com.dailymission.api.springboot.web.repository.mission.QMission;
import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import com.dailymission.api.springboot.web.repository.user.User;
import com.dailymission.api.springboot.web.repository.user.UserRepository;
import com.dailymission.api.springboot.web.user.UserSetup;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * [ 2020-03-20 : 이민호 ]
 * - @DataJpaTest 어노테이션을 통해서 Repository 에 대한 Bean 만 등록합니다.
 * - @DataJpaTest 는 기본적으로 메모리 데이터베이스에 대한 테스트를 진행합니다.
 * - @AutoConfigureTestDatabase 어노테이션을 통해서 profile 에 등록된 데이터베이스 정보로 대체할 수 있습니다.
 * - JpaRepository 에서 기본적으로 기본적으로 제공해주는 findById, findByAll, deleteById 등은 테스트를 하지 않습니다.
 * - 주로 커스텀하게 작성한 쿼리 메서드, QueryDSL 등의 커스텀하게 추가된 메서드를 테스트합니다.
 * - setUp() 메서드를 통해서 Entity 를 데이터베이스에 insert 합니다
 * - 실제로 작성된 쿼리가 어떻게 출력되는지 show-sql 옵션을 통해서 확인 합니다.
 * - ORM 은 SQL 을 직접 장성하지 않으니 실제 쿼리가 어떻게 출력되는지 확인하는 습관을 반드시 가져야합니다.
 * */
@RunWith(SpringRunner.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {JpaConfig.class, QueryDslConfig.class}
))
public class MissionRepositoryTest {

    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager em;
    private JPAQueryFactory queryFactory;

    private Mission mission;
    private MissionRule missionRule;
    private User user;
    private String credential;

    private final String TITLE = "TITLE";
    private final String CONTENT = "MISSION_CONTENT";
    private final String ORIGINAL_FILE_NAME = "ORIGINAL_FILE_NAME.jpg";
    private final String FILE_EXTENSION = ".jpg";
    private final LocalDate START_DATE = LocalDate.of(2020,04,01);
    private final LocalDate END_DATE = LocalDate.of(2020,04,28);
    private final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    private final String IMAGE_URL = "IMAGE_URL.jpg";


    @Before
    public void setup() throws Exception {
        // save user
        user = UserSetup.builder().build().get();
        userRepository.save(user);

        // mission rule (fri/sat :false)
        missionRule = MissionRuleSetup.builder().build().get();

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

        // credential
        credential = mission.setCredential(PASSWORD_ENCODER);

        // save mission
        missionRepository.save(mission);

        // QueryDsl
        queryFactory = new JPAQueryFactory(em);
    }

    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : QueryDSL 의 findAllByCreatedDate() 메서드를 확인한다.
     *
     * Query : select mission0_.id as id1_0_,
     *                mission0_.created_date as created_2_0_,
     *                mission0_.modified_date as modified3_0_,
     *                 mission0_.content as content4_0_,
     *                mission0_.credential as credenti5_0_,
     *                mission0_.deleted as deleted6_0_,
     *                mission0_.end_date as end_date7_0_,
     *                mission0_.ended as ended8_0_,
     *                mission0_.file_extension as file_ext9_0_,
     *                mission0_.image_url as image_u10_0_,
     *                mission0_.mission_rule_id as mission18_0_,
     *                mission0_.original_file_name as origina11_0_,
     *                mission0_.start_date as start_d12_0_,
     *                mission0_.thumbnail_url_all as thumbna13_0_,
     *                mission0_.thumbnail_url_detail as thumbna14_0_,
     *                mission0_.thumbnail_url_hot as thumbna15_0_,
     *                mission0_.thumbnail_url_new as thumbna16_0_,
     *                mission0_.title as title17_0_,
     *                mission0_.user_id as user_id19_0_
     *          from mission mission0_
     *          where mission0_.deleted=?
     *          order by mission0_.created_date desc
     * */
    @Test
    public void save_mission_THEN_call_findAllByCreatedDate(){
        // when
        List<Mission> missions = queryFactory
                                     .select(QMission.mission)
                                     .from(QMission.mission)
                                     .where(QMission.mission.deleted.isFalse())
                                     .orderBy(QMission.mission.createdDate.desc())
                                     .fetch();

        // then
        assertThat(missions.size()).isEqualTo(1);
        assertThat(missions.get(0)).isEqualTo(mission);
    }


    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : QueryDSL 의 findAllCreatedInMonth() 메서드를 확인한다.
     *
     * Query : select mission0_.id as id1_0_,
     *                mission0_.created_date as created_2_0_,
     *                mission0_.modified_date as modified3_0_,
     *                mission0_.content as content4_0_,
     *                mission0_.credential as credenti5_0_,
     *                mission0_.deleted as deleted6_0_,
     *                mission0_.end_date as end_date7_0_,
     *                mission0_.ended as ended8_0_,
     *                mission0_.file_extension as file_ext9_0_,
     *                mission0_.image_url as image_u10_0_,
     *                mission0_.mission_rule_id as mission18_0_,
     *                mission0_.original_file_name as origina11_0_,
     *                mission0_.start_date as start_d12_0_,
     *                mission0_.thumbnail_url_all as thumbna13_0_,
     *                mission0_.thumbnail_url_detail as thumbna14_0_,
     *                mission0_.thumbnail_url_hot as thumbna15_0_,
     *                mission0_.thumbnail_url_new as thumbna16_0_,
     *                mission0_.title as title17_0_,
     *                mission0_.user_id as user_id19_0_
     *       from mission mission0_
     *       where mission0_.deleted=?
     *       and (mission0_.created_date between ? and ?)
     *       order by mission0_.created_date desc
     * */
    @Test
    public void save_mission_THEN_call_findAllCreatedInMonth(){
        // given
        LocalDateTime now =  LocalDateTime.now();
        LocalDateTime monthBefore = now.minusMonths(1);

        // when
        List<Mission> missions = queryFactory
                                        .select(QMission.mission)
                                        .from(QMission.mission)
                                        .where(QMission.mission.deleted.isFalse()
                                                .and(QMission.mission.createdDate.between(monthBefore, now)))
                                        .orderBy(QMission.mission.createdDate.desc())
                                        .fetch();

        // then
        assertThat(missions.size()).isEqualTo(1);
        assertThat(missions.get(0)).isEqualTo(mission);
    }


    /**
     * [ 2020-03-20 : 이민호 ]
     * 설명 : QueryDSL 의 findAllByParticipantSize() 메서드를 확인한다.
     *
     * Query : select mission0_.id as id1_0_,
     *                mission0_.created_date as created_2_0_,
     *                mission0_.modified_date as modified3_0_,
     *                mission0_.content as content4_0_,
     *                mission0_.credential as credenti5_0_,
     *                mission0_.deleted as deleted6_0_,
     *                mission0_.end_date as end_date7_0_,
     *                mission0_.ended as ended8_0_,
     *                mission0_.file_extension as file_ext9_0_,
     *                mission0_.image_url as image_u10_0_,
     *                mission0_.mission_rule_id as mission18_0_,
     *                mission0_.original_file_name as origina11_0_,
     *                mission0_.start_date as start_d12_0_,
     *                mission0_.thumbnail_url_all as thumbna13_0_,
     *                mission0_.thumbnail_url_detail as thumbna14_0_,
     *                mission0_.thumbnail_url_hot as thumbna15_0_,
     *                mission0_.thumbnail_url_new as thumbna16_0_,
     *                mission0_.title as title17_0_,
     *                mission0_.user_id as user_id19_0_
     *         from mission mission0_
     *         where mission0_.deleted=?
     *         order by (
     *                    select count(participan1_.mission_id)
     *                    from participant participan1_
     *                    where mission0_.id=participan1_.mission_id
     *                  ) desc,
     *                  mission0_.created_date desc
     * */
    @Test
    public void save_mission_THEN_call_findAllByParticipantSize(){
        // when
        List<Mission> missions = queryFactory
                                        .select(QMission.mission)
                                        .from(QMission.mission)
                                        .where(QMission.mission.deleted.isFalse())
                                        .orderBy(QMission.mission.participants.size().desc(), QMission.mission.createdDate.desc())
                                        .fetch();

        // then
        assertThat(missions.size()).isEqualTo(1);
        assertThat(missions.get(0)).isEqualTo(mission);
    }
}
