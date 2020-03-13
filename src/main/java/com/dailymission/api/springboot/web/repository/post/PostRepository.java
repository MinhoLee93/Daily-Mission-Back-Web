package com.dailymission.api.springboot.web.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> , PostRepositoryCustom {

    // Post 정보 (detail)
    Optional<Post> findByIdAndDeletedIsFalse(Long id);

}
