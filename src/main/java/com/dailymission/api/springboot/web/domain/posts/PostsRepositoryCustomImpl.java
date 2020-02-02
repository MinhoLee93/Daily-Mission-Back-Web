package com.dailymission.api.springboot.web.domain.posts;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.dailymission.api.springboot.web.domain.posts.QPosts.posts;

public class PostsRepositoryCustomImpl extends QuerydslRepositorySupport implements PostsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PostsRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        super(Posts.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Posts> findAllDesc() {
        List<Posts> postsList = queryFactory
                                .selectFrom(posts)
                                .orderBy(posts.id.desc())
                                .fetch();

        return postsList;
    }
}
