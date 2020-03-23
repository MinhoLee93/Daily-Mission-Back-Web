package com.dailymission.api.springboot.web.post;

import com.dailymission.api.springboot.config.JpaConfig;
import com.dailymission.api.springboot.config.QueryDslConfig;
import com.dailymission.api.springboot.web.dto.post.PostSubmitDto;
import com.dailymission.api.springboot.web.mission.MissionSetup;
import com.dailymission.api.springboot.web.participant.ParticipantSetup;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.MissionRepository;
import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import com.dailymission.api.springboot.web.repository.participant.Participant;
import com.dailymission.api.springboot.web.repository.participant.ParticipantRepository;
import com.dailymission.api.springboot.web.repository.post.Post;
import com.dailymission.api.springboot.web.repository.post.PostRepository;
import com.dailymission.api.springboot.web.repository.post.QPost;
import com.dailymission.api.springboot.web.repository.user.User;
import com.dailymission.api.springboot.web.repository.user.UserRepository;
import com.dailymission.api.springboot.web.user.UserSetup;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
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
public class PostRepositoryTest {
    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private EntityManager em;
    private JPAQueryFactory queryFactory;

    private Mission mission;
    private MissionRule missionRule;
    private Participant participant;
    private User user;
    private Post post;
    private LocalDateTime startDate;

    @Before
    public void setup() throws Exception {
        // start time
        startDate = LocalDateTime.now();

        // save user
        user = userRepository.save(UserSetup.builder().build().get());

        // save mission
        mission =  missionRepository.save(MissionSetup.builder().user(user).build().get());

        // save participant
        participant =  participantRepository.save(ParticipantSetup.builder().user(user).mission(mission).build().get());

        // save post
        post = postRepository.save(PostSetup.builder().mission(mission).user(user).build().get());

        // QueryDsl
        queryFactory = new JPAQueryFactory(em);
    }

    /**
     * [ 2020-03-23 : 이민호 ]
     * 설명 : QueryDSL 의 findAll() 메서드를 확인한다.
     *        전체 POST 목록을 조회한다.
     *
     * Query : select post0_.id as id1_3_,
     *                post0_.created_date as created_2_3_,
     *                post0_.modified_date as modified3_3_,
     *                post0_.content as content4_3_,
     *                post0_.deleted as deleted5_3_,
     *                post0_.file_extension as file_ext6_3_,
     *                post0_.image_url as image_ur7_3_,
     *                post0_.mission_id as mission11_3_,
     *                post0_.original_file_name as original8_3_,
     *                post0_.thumbnail_url as thumbnai9_3_,
     *                post0_.title as title10_3_,
     *                post0_.user_id as user_id12_3_
     *        from post post0_
     *        where post0_.deleted=?
     *        order by post0_.created_date desc
     * */
    @Test
    public void save_post_THEN_call_findAll(){
        // when
        List<Post> posts = queryFactory
                                .select(QPost.post)
                                .from(QPost.post)
                                .where(QPost.post.deleted.isFalse())
                                .orderBy(QPost.post.createdDate.desc()).fetch();

        // then
        assertThat(posts.size()).isEqualTo(1);
        assertThat(posts.get(0)).isEqualTo(post);
    }


    /**
     * [ 2020-03-23 : 이민호 ]
     * 설명 : QueryDSL 의 findAllByMission() 메서드를 확인한다.
     *        미션별 POST 목록을 조회한다.
     *
     * Query : select post0_.id as id1_3_,
     *                post0_.created_date as created_2_3_,
     *                post0_.modified_date as modified3_3_,
     *                post0_.content as content4_3_,
     *                post0_.deleted as deleted5_3_,
     *                post0_.file_extension as file_ext6_3_,
     *                post0_.image_url as image_ur7_3_,
     *                post0_.mission_id as mission11_3_,
     *                post0_.original_file_name as original8_3_,
     *                post0_.thumbnail_url as thumbnai9_3_,
     *                post0_.title as title10_3_,
     *                post0_.user_id as user_id12_3_
     *         from post post0_
     *         where post0_.mission_id=? and post0_.deleted=?
     *         order by post0_.created_date desc
     * */
    @Test
    public void save_post_THEN_find_all_by_mission(){
        // when
        List<Post> posts = queryFactory
                                .select(QPost.post)
                                .from(QPost.post)
                                // eq(mission)
                                .where(QPost.post.mission.eq(mission).and(QPost.post.deleted.isFalse()))
                                .orderBy(QPost.post.createdDate.desc()).fetch();

        // then
        assertThat(posts.size()).isEqualTo(1);
        assertThat(posts.get(0)).isEqualTo(post);
    }



    /**
     * [ 2020-03-23 : 이민호 ]
     * 설명 : QueryDSL 의 findAllByUser() 메서드를 확인한다.
     *        유저별 POST 목록을 조회한다.
     *
     * Query : select post0_.id as id1_3_,
     *                post0_.created_date as created_2_3_,
     *                post0_.modified_date as modified3_3_,
     *                post0_.content as content4_3_,
     *                post0_.deleted as deleted5_3_,
     *                post0_.file_extension as file_ext6_3_,
     *                post0_.image_url as image_ur7_3_,
     *                post0_.mission_id as mission11_3_,
     *                post0_.original_file_name as original8_3_,
     *                post0_.thumbnail_url as thumbnai9_3_,
     *                post0_.title as title10_3_,
     *                post0_.user_id as user_id12_3_
     *         from post post0_
     *         where post0_.user_id=?
     *               and post0_.deleted=?
     *         order by post0_.created_date desc
     * */
    @Test
    public void save_post_THEN_find_all_by_user(){
        // when
        List<Post> posts = queryFactory
                                .select(QPost.post)
                                .from(QPost.post)
                                // eq(mission)
                                .where(QPost.post.user.eq(user).and(QPost.post.deleted.isFalse()))
                                .orderBy(QPost.post.createdDate.desc()).fetch();

        // then
        assertThat(posts.size()).isEqualTo(1);
        assertThat(posts.get(0)).isEqualTo(post);
    }


    /**
     * [ 2020-03-23 : 이민호 ]
     * 설명 : QueryDSL 의 countPostSubmit() 메서드를 확인한다.
     *        금일 해당 미션에 제출 기록이 있는지 확인한다.
     *        삭제된 포스트는 제출하지 않은 것으로 간주한다.
     *
     * Query : select count(post0_.id) as col_0_0_
     *         from post post0_
     *         where post0_.mission_id=?
     *               and post0_.user_id=?
     *               and post0_.deleted=?
     *               and post0_.created_date>?
     *               and post0_.created_date<?
     * */
    @Test
    public void save_post_THEN_count_post_submit(){
        // given
        LocalDateTime endDate = startDate.plusSeconds(1);

        // when
        Long count = queryFactory
                            .select(QPost.post.countDistinct())
                            .from(QPost.post)
                            .where(QPost.post.mission.eq(mission)
                                    .and(QPost.post.user.eq(user))
                                    // count only not deleted
                                    .and(QPost.post.deleted.isFalse())
                                    .and(QPost.post.createdDate.after(startDate)
                                            .and(QPost.post.createdDate.before(endDate))))
                            .fetchCount();

        // then
        assertThat(count).isEqualTo(1);
    }


    /**
     * [ 2020-03-23 : 이민호 ]
     * 설명 : QueryDSL 의 findWeeklyPostSubmitByMission() 메서드를 확인한다.
     *        미션별 weekly post submit 를 PostSubmitDto 객체로 전달받는다.
     *        0시 ~ 03 시 제출 기록은 이전날짜 제출 기록으로 변환한다.
     *
     * Query : select post0_.created_date as col_0_0_,
     *                post0_.user_id as col_1_0_,
     *                user1_.name as col_2_0_,
     *                user1_.image_url as col_3_0_
     *        from post post0_ cross join user user1_
     *        where post0_.user_id=user1_.id
     *              and post0_.mission_id=?
     *              and post0_.created_date>?
     *              and post0_.created_date<?
     *        order by post0_.created_date desc
     * */
    @Test
    public void save_post_THEN_find_weekly_post_submit_by_mission(){

        // given
        LocalDateTime startDateTime = LocalDateTime.of(startDate.getYear(),startDate.getMonth(),startDate.getDayOfMonth(), 03, 00, 00);
        LocalDateTime endDateTime = LocalDateTime.of(startDate.plusDays(7).getYear(), startDate.plusDays(7).getMonth(), startDate.plusDays(7).getDayOfMonth(), 03, 00 ,00);
        Long id = mission.getId();

        // when
        List<PostSubmitDto> postSubmitDtos = queryFactory
                            .select(Projections.constructor(PostSubmitDto.class,
                                    QPost.post.createdDate.as("date"),
                                    QPost.post.user.id.as("userId"),
                                    QPost.post.user.name.as("userName"),
                                    QPost.post.user.imageUrl.as("thumbnailUrl")
                            ))
                            .from(QPost.post)
                            .where(QPost.post.mission.id.in(id).and(QPost.post.createdDate.after(startDateTime).and(QPost.post.createdDate.before(endDateTime))))
                            .orderBy(QPost.post.createdDate.desc())
                            .fetch();

        // then
        assertThat(postSubmitDtos.size()).isEqualTo(1);
        assertThat(postSubmitDtos.get(0).getUserId()).isEqualTo(user.getId());
        assertThat(postSubmitDtos.get(0).getUserName()).isEqualTo(user.getName());
        assertThat(postSubmitDtos.get(0).getThumbnailUrl()).isEqualTo(user.getImageUrl());
    }
}
