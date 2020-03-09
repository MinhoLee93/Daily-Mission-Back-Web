package com.dailymission.api.springboot.web.repository.mission;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission, Long>, MissionRepositoryCustom {
    Mission save(Mission mission);

    Optional<Mission> findByIdAAndDeleted(Long id, Boolean deleted);
}
