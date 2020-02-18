package com.dailymission.api.springboot.web.mission;

import com.dailymission.api.springboot.web.dto.mission.MissionSaveRequestDto;
import com.dailymission.api.springboot.web.dto.mission.MissionUpdateRequestDto;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.MissionRepository;
import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import com.dailymission.api.springboot.web.service.mission.MissionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class MissionServiceTest {

    @InjectMocks
    private MissionService missionService;

    @Mock
    private MissionRepository missionRepository;

    @Mock
    private MissionRule missionRule;

    private Mission mission;


    @Before
    public void setup() throws Exception {
        mission = Mission.builder().build();
    }

    @Test
    public void mission_저장_성공() throws Exception {
        // given
        MissionSaveRequestDto requestDto = MissionSaveRequestDto.builder().build();

        given(missionRepository.save(any())).willReturn(mission);

        // when
        Long id = missionService.save(requestDto);

        // then
        assertThat(id).isEqualTo(mission.getId());
    }

    @Test(expected = NoSuchElementException.class)
    public void mission_조회시_조회된_결과가_없을경우_에러가발생하는지(){
        // given
        given(missionRepository.findById(any())).willReturn(null);

        // when
        missionService.findById(1L);
    }

    @Test(expected = NoSuchElementException.class)
    public void mission_업데이트시_업데이트할_미션이_없을경우_에러가발생하는지(){
        // given
        given(missionRepository.findById(any())).willReturn(null);
        Long id = 1L;
        MissionUpdateRequestDto requestDto = MissionUpdateRequestDto.builder().build();

        // when
        missionService.update(1L, requestDto);
    }

    @Test
    public void mission_업데이트시_업데이트할_미션이_있을경우_정상적으로_업데이트_하는지(){
        // given
        Mission mission = Mission.builder().build();
        Long id = 1L;
        MissionUpdateRequestDto requestDto = MissionUpdateRequestDto.builder()
                                                                    .title("update")
                                                                    .content("update")
                                                                    .build();

        given(missionRepository.findById(any())).willReturn(java.util.Optional.ofNullable(mission));

        // when
        missionService.update(1L, requestDto);

        // then
        assertThat(mission.getTitle()).isEqualTo("update");
        assertThat(mission.getContent()).isEqualTo("update");
    }

    @Test
    public void mission_delete_한뒤_mission의_deleteflag가_Y로_변경되었는지(){
        // given
        Mission mission = Mission.builder().build();

        given(missionRepository.findById(any())).willReturn(java.util.Optional.ofNullable(mission));

        // when
        missionService.delete(1L);

        // then
        assertThat(mission.isDeleted()).isTrue();
    }

    @Test
    public void mission_end_한뒤_mission의_endflag가_Y로_변경되었는지(){
        // given
        Mission mission = Mission.builder().build();

        given(missionRepository.findById(any())).willReturn(java.util.Optional.ofNullable(mission));

        // when
        missionService.end(1L);

        // then
        assertThat(mission.isEnded()).isTrue();
    }
}
