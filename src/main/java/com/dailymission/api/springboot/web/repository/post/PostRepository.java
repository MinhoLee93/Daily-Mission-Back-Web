package com.dailymission.api.springboot.web.repository.post;

import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> , PostRepositoryCustom {

    // 특정 미션의 전체 post 목록
    List<Post> findAllByMissionAndDeletedIsFalse(Mission mission);

    // 특정 유저의 전체 post 목록
    List<Post> findAllByUserEqualsAndDeletedIsFalse(User user);


    // 전체 post 목록 (삭제안된 것)
    Optional<Post> findByIdAndDeletedIsFalse(Long id);

}
