package com.dailymission.api.springboot.web.domain.post;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class PostRepositoryCustomImpl extends QuerydslRepositorySupport implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PostRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        super(Post.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Post> findAllDesc() {
        QPost post = QPost.post;

        List<Post> postList = queryFactory.selectFrom(post).orderBy(post.id.desc()).fetch();
        return postList;
    }
}
