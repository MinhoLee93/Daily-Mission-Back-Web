package com.dailymission.api.springboot.web.repository.participant;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;

public class ParticipantRepositoryCustomImpl extends QuerydslRepositorySupport implements ParticipantRepositoryCustom {

    @Autowired
    EntityManager em;

    private JPAQueryFactory queryFactory;

    public ParticipantRepositoryCustomImpl(){
        super(Participant.class);
        this.queryFactory = new JPAQueryFactory(em);
    }
}
