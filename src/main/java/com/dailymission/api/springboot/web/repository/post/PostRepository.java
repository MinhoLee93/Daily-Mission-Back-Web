package com.dailymission.api.springboot.web.repository.post;

import com.dailymission.api.springboot.web.repository.mission.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> , PostRepositoryCustom {
    // 특정 미션의 post 전체 출력
    List<Post> findAllByMission(Mission mission);
}
