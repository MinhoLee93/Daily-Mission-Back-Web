package com.dailymission.api.springboot.web.domain.posts;

import java.util.List;

public interface PostsRepositoryCustom  {
    // all posts desc by id
    List<Posts> findAllDesc();
}
