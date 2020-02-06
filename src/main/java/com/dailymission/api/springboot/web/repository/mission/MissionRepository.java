package com.dailymission.api.springboot.web.repository.mission;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<Mission, Long>, MissionRepositoryCustom {

}
