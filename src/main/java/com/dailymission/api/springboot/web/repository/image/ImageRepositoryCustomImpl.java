package com.dailymission.api.springboot.web.repository.image;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;

public class ImageRepositoryCustomImpl extends QuerydslRepositorySupport implements  ImageRepositoryCustom {

    @Autowired
    EntityManager em;

    private JPAQueryFactory queryFactory;

    public ImageRepositoryCustomImpl(){
        super(Image.class);
        this.queryFactory = new JPAQueryFactory(em);
    }
}
