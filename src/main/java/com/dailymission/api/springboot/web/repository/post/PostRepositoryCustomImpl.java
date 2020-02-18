package com.dailymission.api.springboot.web.repository.post;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import java.util.List;

import static com.dailymission.api.springboot.web.repository.post.QPost.post;


@RequiredArgsConstructor
public class PostRepositoryCustomImpl  implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Post> findAllDescAndDeletedIsFalse() {

        return queryFactory
                .select(post)
                .from(post)
                .where(post.deleted.isFalse())
                .orderBy(post.id.desc()).fetch();
    }
}
