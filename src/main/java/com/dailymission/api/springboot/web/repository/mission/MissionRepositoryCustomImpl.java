package com.dailymission.api.springboot.web.repository.mission;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.dailymission.api.springboot.web.repository.mission.QMission.mission;

@RequiredArgsConstructor
public class MissionRepositoryCustomImpl implements MissionRepositoryCustom {

    /**
     * [ 2020-03-13 : 이민호 ]
     * 설명 : QueryDSL 테스트에서 DI 에러 발생시
     *       주석을 풀고 사용한다.
     * */
//    @Autowired
//    private EntityManager em;

    private final JPAQueryFactory queryFactory;

    /**
     * [ 2020-03-13 : 이민호 ]
     * 설명 : QueryDSL 테스트에서 DI 에러 발생시
     *       주석을 풀고 사용한다.
     * */
//    public MissionRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory){
//        super(Mission.class);
//        this.queryFactory = jpaQueryFactory;
//    }

    /**
     * [ 2020-03-16 : 이민호 ]
     * 설명 : 참여자가 많은 순서대로 MISSION 목록을 조회한다.
     *        조건 : 삭제되지 않은 미션
     *        정렬 : 참여자 많은 순서 -> 최신 생성 순서
     * */
    @Override
    public List<Mission> findAllByParticipantSize() {

        return  queryFactory
                .select(mission)
                .from(mission)
                .where(mission.deleted.isFalse().and(mission.ended.isFalse()))
                .orderBy(mission.participants.size().desc(), mission.createdDate.desc())
                .fetch();

    }

    /**
     * [ 2020-03-16 : 이민호 ]
     * 설명 : 금일부터 한달이내에 생성된 MISSION 목록을 조회한다.
     *        조건 : 삭제되지 않은 미션, 생성일자가 최근 한달 사이
     *        정렬 : 최신 생성 순서
     * */
    @Override
    public List<Mission> findAllCreatedInMonth() {

        LocalDateTime now =  LocalDateTime.now();
        LocalDateTime monthBefore = now.minusMonths(1);

        return  queryFactory
                .select(mission)
                .from(mission)
                .where(mission.deleted.isFalse()
                        .and(mission.ended.isFalse())
                        .and(mission.createdDate.between(monthBefore, now)))
                .orderBy(mission.createdDate.desc())
                .fetch();
    }

    /**
     * [ 2020-03-13 : 이민호 ]
     * 설명 : 전체 미션 목록을 조회한다.
     *        조건 : 삭제되지 않은 미션
     *        정렬 : 최신 생성 순서
     * */
    @Override
    public List<Mission> findAllByCreatedDate() {

       return  queryFactory
                .select(mission)
                .from(mission)
                .where(mission.deleted.isFalse())
                .orderBy(mission.createdDate.desc())
                .fetch();
    }
}

