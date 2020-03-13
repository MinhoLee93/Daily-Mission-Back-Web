package com.dailymission.api.springboot.web.repository.mission;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

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
//    public MissionRepositoryCustomImpl(){
//        super(Mission.class);
//        this.queryFactory = new JPAQueryFactory(em);
//    }

    /**
     * [ 2020-03-13 : 이민호 ]
     * 설명 : MISSION 에서 삭제되지 않은 미션들을
     *        Descending by Created date 으로 조회 한다.
     * */
    @Override
    public List<Mission> findAll() {
       return  queryFactory
                .select(mission)
                .from(mission)
                .where(mission.deleted.isFalse())
                .orderBy(mission.createdDate.desc())
                .fetch();
    }
}

