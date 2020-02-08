package com.dailymission.api.springboot.web.repository.mission;

import java.util.List;

public interface MissionRepositoryCustom {
    // all mission desc by id
    List<Mission> findAllDesc();
}
