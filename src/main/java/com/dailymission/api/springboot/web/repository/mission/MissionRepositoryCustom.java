package com.dailymission.api.springboot.web.repository.mission;

import java.util.List;

public interface MissionRepositoryCustom {
    // 전체 미션 목록 (order by createdDate)
    List<Mission> findAllByCreatedDate();
}
