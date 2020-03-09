package com.dailymission.api.springboot.web.repository.mission;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission, Long>, MissionRepositoryCustom {

    // Mission 저장
    Mission save(Mission mission);

    // Mission 정보 (detail)
    Optional<Mission> findByIdAAndDeletedIsFalse(Long id);
}
