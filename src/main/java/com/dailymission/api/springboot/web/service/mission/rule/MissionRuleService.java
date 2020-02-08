package com.dailymission.api.springboot.web.service.mission.rule;

import com.dailymission.api.springboot.web.dto.mission.rule.*;
import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import com.dailymission.api.springboot.web.repository.mission.rule.MissionRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class MissionRuleService {

    MissionRuleRepository missionRuleRepository;

    @Transactional
    public Long save(MissionRuleSaveRequestDto requestDto){
        return missionRuleRepository.save(requestDto.toEntitiy()).getId();
    }


    @Transactional(readOnly = true)
    public MissionRuleResponseDto findById (Long id){
        Optional<MissionRule> optional = Optional.ofNullable(missionRuleRepository.findById(id))
                .orElseThrow(() -> new NoSuchElementException("해당 룰이 없습니다. id=" + id));

        MissionRule missionRule = optional.get();
        return new MissionRuleResponseDto(missionRule);
    }


    @Transactional
    public Long update(Long id, MissionRuleUpdateRequestDto requestDto){
        Optional<MissionRule> optional = Optional.ofNullable(missionRuleRepository.findById(id))
                   .orElseThrow(() -> new NoSuchElementException("해당 룰이 없습니다. id=" + id));

        MissionRule missionRule = optional.get();
        missionRule.update(requestDto.getWeek());

        return id;
    }


    @Transactional
    public void delete(Long id){
        Optional<MissionRule> optional = Optional.ofNullable(missionRuleRepository.findById(id))
                .orElseThrow(() -> new NoSuchElementException("해당 룰이 없습니다. id=" + id));

        MissionRule missionRule = optional.get();

        // delete flag -> 'Y'
        missionRule.delete();
    }
}
