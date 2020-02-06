package com.dailymission.api.springboot.web.service;

import com.dailymission.api.springboot.web.dto.missionRule.MissionRuleResponseDto;
import com.dailymission.api.springboot.web.dto.post.PostResponseDto;
import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import com.dailymission.api.springboot.web.repository.mission.rule.MissionRuleRepository;

import com.dailymission.api.springboot.web.dto.missionRule.MissionRuleSaveRequestDto;
import com.dailymission.api.springboot.web.dto.missionRule.MissionRuleUpdateRequestDto;
import com.dailymission.api.springboot.web.repository.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.NoSuchElementException;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class MissionRuleService {

    MissionRuleRepository missionRuleRepository;

    @Transactional
    public Long update(Long id, MissionRuleUpdateRequestDto requestDto){
        Optional<MissionRule> optional = Optional.ofNullable(missionRuleRepository.findById(id))
                   .orElseThrow(() -> new NoSuchElementException("해당 룰이 없습니다. id=" + id));

        MissionRule rule = optional.get();
        rule.update(requestDto.getWeek());
        return id;
    }

    @Transactional
    public Long save(MissionRuleSaveRequestDto requestDto){
        return missionRuleRepository.save(requestDto.toEntitiy()).getId();
    }

    @Transactional(readOnly = true)
    public MissionRuleResponseDto findById (Long id){
        MissionRule entity = missionRuleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 룰이 없습니다. id=" + id));

        return new MissionRuleResponseDto(entity);
    }

    @Transactional
    public void delete(Long id){
        MissionRule missionRule = missionRuleRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("해당 룰이 없습니다. id =" + id));

        // delete flag -> 'Y'
        missionRule.delete();
    }
}
