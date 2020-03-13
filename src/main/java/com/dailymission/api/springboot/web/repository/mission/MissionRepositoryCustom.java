package com.dailymission.api.springboot.web.repository.mission;

import java.util.List;

/**
 * [ 2020-03-13 : 이민호 ]
 * 설명 : 전체 조회시 Default Ordering 기준은
 *        Descending by Created Date + Not Deleted 이므로
 *        이를 생략해서 메서드 이름을 간소화 한다.
 * */
public interface MissionRepositoryCustom {

    // 전체 미션 목록
    List<Mission> findAll();

}
