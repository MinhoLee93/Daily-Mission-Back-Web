package com.dailymission.api.springboot.web.repository.post;

import com.dailymission.api.springboot.web.dto.post.PostHistoryDto;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepositoryCustom {
    // all post desc by id
    List<Post> findAllDescAndDeletedIsFalse();

    // 스케줄
    List<PostHistoryDto> findSchedule(Long id, String startDate, String endDate);

    // 제출기록
    List<Post> findSubmitHistory(Long missionId, Long userId, LocalDateTime startDate, LocalDateTime endDate);
}
