package com.dailymission.api.springboot.web.repository.mission;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import java.util.List;

public class MissionRepositoryCustomImpl extends QuerydslRepositorySupport implements MissionRepositoryCustom {

    @Autowired
    EntityManager em;

    private JPAQueryFactory queryFactory;

    public MissionRepositoryCustomImpl(){
        super(Mission.class);
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public List<Mission> findAllDesc() {
        QMission mission = QMission.mission;

        List<Mission> missionList = queryFactory.selectFrom(mission).orderBy(mission.id.desc()).fetch();
        return missionList;
    }
}

