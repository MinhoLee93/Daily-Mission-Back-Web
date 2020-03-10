package com.dailymission.api.springboot.web.repository.post;

import com.dailymission.api.springboot.web.dto.post.PostHistoryDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.dailymission.api.springboot.web.repository.post.QPost.post;


@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Post> findAllDescAndDeletedIsFalse() {

        return queryFactory
                .select(post)
                .from(post)
                .where(post.deleted.isFalse())
                .orderBy(post.id.desc()).fetch();
    }

    @Override
    public List<PostHistoryDto> findSchedule(Long id, LocalDateTime startDate, LocalDateTime endDate) {

        return queryFactory
                .select(Projections.constructor(PostHistoryDto.class,
                        post.createdDate.as("date"),
                        post.user.id.as("userId"),
                        post.user.name.as("userName"),
                        post.user.imageUrl.as("imageUrl")
                        ))
                .from(post)
                .where(post.mission.id.in(id).and(post.createdDate.after(startDate).and(post.createdDate.before(endDate))))
                .orderBy(post.createdDate.desc())
                .fetch();

    }

    @Override
    public List<Post> findSubmitHistory(Long missionId, Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return queryFactory
                .select(post)
                .from(post)
                .where(post.mission.id.eq(missionId)
                        .and(post.user.id.eq(userId))
                        .and(post.createdDate.after(startDate)
                                .and(post.createdDate.before(endDate))))
                        .fetch();
    }
}
