package com.dailymission.api.springboot.web.domain.mission;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class MissionRepositoryCustomImpl extends QuerydslRepositorySupport implements MissionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MissionRepositoryCustomImpl(JPAQueryFactory queryFactory){
        super(Mission.class);
        this.queryFactory = queryFactory;
    }
}
