package com.dailymission.api.springboot.web.repository.participant;

import com.dailymission.api.springboot.web.repository.account.Account;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    Optional<Participant> findByMissionAndAccount(Mission mission, Account account);

    List<Participant> findAllByMission(Mission mission);

    List<Participant> findAllByAccount(Account account);

}
