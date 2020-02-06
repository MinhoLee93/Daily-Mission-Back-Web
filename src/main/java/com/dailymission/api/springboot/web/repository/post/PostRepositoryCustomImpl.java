package com.dailymission.api.springboot.web.repository.post;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import java.util.List;

public class PostRepositoryCustomImpl extends QuerydslRepositorySupport implements PostRepositoryCustom {

    @Autowired
    EntityManager em;

    private JPAQueryFactory queryFactory;

    public PostRepositoryCustomImpl() {
        super(Post.class);
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Post> findAllDesc() {
        QPost post = QPost.post;

        List<Post> postList = queryFactory.selectFrom(post).orderBy(post.id.desc()).fetch();
        return postList;
    }
}
