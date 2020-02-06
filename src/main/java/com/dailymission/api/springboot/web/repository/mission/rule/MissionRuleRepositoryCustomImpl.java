package com.dailymission.api.springboot.web.repository.mission.rule;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;

public class MissionRuleRepositoryCustomImpl extends QuerydslRepositorySupport implements MissionRuleRepositoryCustom {

    @Autowired
    EntityManager em;

    private JPAQueryFactory queryFactory;

    public MissionRuleRepositoryCustomImpl(){
        super(MissionRule.class);
        this.queryFactory = new JPAQueryFactory(em);
    }
}
