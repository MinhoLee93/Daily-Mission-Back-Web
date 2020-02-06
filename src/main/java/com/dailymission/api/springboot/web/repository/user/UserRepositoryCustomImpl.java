package com.dailymission.api.springboot.web.repository.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;

public class UserRepositoryCustomImpl extends QuerydslRepositorySupport implements  UserRepositoryCustom {

    @Autowired
    EntityManager em;

    private JPAQueryFactory queryFactory;

    public UserRepositoryCustomImpl(){
        super(User.class);
        this.queryFactory = new JPAQueryFactory(em);
    }
}
