package com.dailymission.api.springboot.web.domain.posts;

import com.dailymission.api.springboot.web.domain.posts.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> , PostsRepositoryCustom {

}
