package com.dailymission.api.springboot.web.repository.post;

import com.dailymission.api.springboot.web.dto.post.PostSubmitDto;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * [ 2020-03-13 : 이민호 ]
 * 설명 : 전체 조회시 Default Ordering 기준은
 *        Descending by Created Date + Not Deleted 이므로
 *        이를 생략해서 메서드 이름을 간소화 한다.
 * */
public interface PostRepositoryCustom {

    // 전체 POST 목록
    List<Post> findAll();

    // 미션별 전체 POST 목록
    List<Post> findAllByMission(Mission mission);

    // 유저별 전체 POST 목록
    List<Post> findAllByUser(User user);

    /**
     * [ 2020-03-13 : 이민호 ]
     * 설명 : 미션별로 startDate 이후 일주일간의 포스트 제출 기록을 확인한다.
     * */
    List<PostSubmitDto> findWeeklyPostSubmitByMission(Long id, LocalDate startDate);

    // 제출기록
    Long countPostSubmit(Mission mission, User user, LocalDateTime startDate, LocalDateTime endDate);

}
