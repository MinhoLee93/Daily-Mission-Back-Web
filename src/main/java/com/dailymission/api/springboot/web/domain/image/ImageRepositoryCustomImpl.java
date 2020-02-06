package com.dailymission.api.springboot.web.domain.image;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class ImageRepositoryCustomImpl extends QuerydslRepositorySupport implements  ImageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ImageRepositoryCustomImpl(JPAQueryFactory queryFactory){
        super(Image.class);
        this.queryFactory = queryFactory;
    }
}
