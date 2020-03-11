package com.dailymission.api.springboot.web.repository.post;

import com.dailymission.api.springboot.web.dto.post.PostHistoryDto;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepositoryCustom {
    // all post desc by id
    List<Post> findAllDescAndDeletedIsFalse();

    // 스케줄
    List<PostHistoryDto> findSchedule(Long id, LocalDateTime startDate, LocalDateTime endDate);

    // 제출기록
    Long countPostSubmit(Long missionId, Long userId, LocalDateTime startDate, LocalDateTime endDate);
}
