package com.dailymission.api.springboot.web.repository.participant;

import com.dailymission.api.springboot.web.repository.user.User;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    Optional<Participant> findByMissionAndUser(Mission mission, User user);

    List<Participant> findAllByMission(Mission mission);

    List<Participant> findAllByUser(User user);

}
