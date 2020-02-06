package com.dailymission.api.springboot.web.domain.mission.rule;

import com.dailymission.api.springboot.web.domain.mission.Mission;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class MissionRuleRepositoryCustomImpl extends QuerydslRepositorySupport implements MissionRuleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MissionRuleRepositoryCustomImpl(JPAQueryFactory queryFactory){
        super(Mission.class);
        this.queryFactory = queryFactory;
    }
}
