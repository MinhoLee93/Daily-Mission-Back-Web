package com.dailymission.api.springboot.web.domain.post;

import java.util.List;

public interface PostRepositoryCustom {
    // all post desc by id
    List<Post> findAllDesc();
}
