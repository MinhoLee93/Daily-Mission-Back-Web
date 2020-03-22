package com.dailymission.api.springboot.web.repository.post;

import com.dailymission.api.springboot.web.dto.post.PostSubmitDto;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.user.User;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.dailymission.api.springboot.web.repository.post.QPost.post;


@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

//    @Autowired
//    EntityManager em;

    private JPAQueryFactory queryFactory;

//    public PostRepositoryCustomImpl(){
//        super(Post.class);
//        this.queryFactory = new JPAQueryFactory(em);
//    }

    /**
     * [ 2020-03-13 : 이민호 ]
     * 설명 : 전체 POST 목록을 조회한다.
     * */
    @Override
    public List<Post> findAll() {

        return queryFactory
                .select(post)
                .from(post)
                .where(post.deleted.isFalse())
                .orderBy(post.createdDate.desc()).fetch();

    }

    /**
     * [ 2020-03-13 : 이민호 ]
     * 설명 : 미션별 POST 목록을 조회한다.
     * */
    @Override
    public List<Post> findAllByMission(Mission mission) {

        return queryFactory
                .select(post)
                .from(post)
                // eq(mission)
                .where(post.mission.eq(mission).and(post.deleted.isFalse()))
                .orderBy(post.createdDate.desc()).fetch();

    }

    /**
     * [ 2020-03-13 : 이민호 ]
     * 설명 : 유저별 POST 목록을 조회한다.
     * */
    @Override
    public List<Post> findAllByUser(User user) {

        return queryFactory
                .select(post)
                .from(post)
                // eq(user)
                .where(post.user.eq(user).and(post.deleted.isFalse()))
                .orderBy(post.createdDate.desc()).fetch();

    }

    /**
     * [ 2020-03-13 : 이민호 ]
     * 설명 : 미션별 weekly post submit 를 PostSubmitDto 객체로 전달받는다.
     *        0시 ~ 03 시 제출 기록은 이전날짜 제출 기록으로 변환한다.
     * */
    @Override
    public List<PostSubmitDto> findWeeklyPostSubmitByMission(Long id, LocalDate startDate) {

        // get end date
        LocalDate endDate = startDate.plusDays(7);

        // get start & end date time (03:00)
        LocalDateTime startDateTime = LocalDateTime.of(startDate.getYear(),startDate.getMonth(),startDate.getDayOfMonth(), 03, 00, 00);
        LocalDateTime endDateTime = LocalDateTime.of(endDate.getYear(), endDate.getMonth(), endDate.getDayOfMonth(), 03, 00 ,00);

        // find post history between start & end
        return queryFactory
                .select(Projections.constructor(PostSubmitDto.class,
                        post.createdDate.as("date"),
                        post.user.id.as("userId"),
                        post.user.name.as("userName"),
                        post.user.imageUrl.as("thumbnailUrl")
                        ))
                .from(post)
                .where(post.mission.id.in(id).and(post.createdDate.after(startDateTime).and(post.createdDate.before(endDateTime))))
                .orderBy(post.createdDate.desc())
                .fetch();

    }

    /**
     * [ 2020-03-13 : 이민호 ]
     * 설명 : 금일 해당 미션에 제출 기록이 있는지 확인한다.
     *        삭제된 포스트는 제출하지 않은 것으로 간주한다.
     * */
    @Override
    public Long countPostSubmit(Mission mission, User user, LocalDateTime startDate, LocalDateTime endDate) {

        return queryFactory
                .select(post.countDistinct())
                .from(post)
                .where(post.mission.eq(mission)
                        .and(post.user.eq(user))
                        // count only not deleted
                        .and(post.deleted.isFalse())
                        .and(post.createdDate.after(startDate)
                                .and(post.createdDate.before(endDate))))
                        .fetchCount();

    }
}
