package com.dailymission.api.springboot.web.repository.mission;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.dailymission.api.springboot.web.repository.mission.QMission.mission;

@RequiredArgsConstructor
public class MissionRepositoryCustomImpl implements MissionRepositoryCustom {

//    @Autowired
//    private EntityManager em;

    private final JPAQueryFactory queryFactory;

//    public MissionRepositoryCustomImpl(){
//        super(Mission.class);
//        this.queryFactory = new JPAQueryFactory(em);
//    }

    @Override
    public List<Mission> findAllDesc() {
       return  queryFactory
                .select(mission)
                .from(mission)
                .orderBy(mission.id.desc())
                .fetch();
    }
}

