package com.dailymission.api.springboot.web.mission;


import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.MissionRepository;
import com.dailymission.api.springboot.web.repository.user.User;
import com.dailymission.api.springboot.web.repository.user.UserRepository;
import com.dailymission.api.springboot.web.user.UserSetup;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MissionControllerTest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MissionRepository missionRepository;

    private MissionSetup missionSetup;

    private UserSetup userSetup;

    private Mission mission;

    private User user;

//    @Before
//    public void setup(){
//        userSetup = UserSetup.builder().build();
//
//        user = userRepository.save(userSetup.build());
//
//        missionSetup = MissionSetup.builder().user(user).build();
//
//        mission = missionSetup.build();
//    }
//
//    @Test
//    public void mission_생성후_정상등록_확인() throws Exception {
//        // given
//        mission = missionSetup.build();
//
//        MissionSaveRequestDto requestDto = MissionSaveRequestDto.builder()
//                                                                .week(mission.getMissionRule().getWeek())
//                                                                .title(mission.getTitle())
//                                                                .content(mission.getContent())
//                                                                .startDate(mission.getStartDate())
//                                                                .endDate(mission.getEndDate())
//                                                                .build();
//
//        // when
//        mvc.perform(
//                post("/api/mission")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(new Gson().toJson(requestDto)))
//                .andDo(print())
//                .andExpect(status().isOk());
//
//        // then
//        List<Mission> all = missionRepository.findAll();
//        assertThat(all.get(0).getTitle()).isEqualTo("test");
//        assertThat(all.get(0).getContent()).isEqualTo("test");
//
//        System.out.println(new Gson().toJson(all.get(0)));
//    }
//
//    @Test
//    public void mission_수정후_값__확인() throws Exception{
//        // given
//
//        // Y/Y/Y/Y/Y/N/N
//        Mission savedMission = missionRepository.save(mission);
//
//        // Y/Y/Y/Y/Y/Y/Y
//        MissionRule updateMissionRule = MissionRuleSetup.builder().build().build();
//
//        MissionUpdateRequestDto requestDto = MissionUpdateRequestDto.builder()
//                                                                  .missionRule(updateMissionRule)
//                                                                  .title("update")
//                                                                  .content("update")
//                                                                  .startDate(LocalDate.of(2020,01,01))
//                                                                  .endDate(LocalDate.of(2020,03,28))
//                                                                  .build();
//        // when
//        mvc.perform(
//                put("/api/mission/" +  savedMission.getId())
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(new Gson().toJson(requestDto)))
//                .andExpect(status().isOk());
//
//        // then
//        List<Mission> all = missionRepository.findAll();
//        assertThat(all.get(0).getTitle()).isEqualTo("update");
//        assertThat(all.get(0).getContent()).isEqualTo("update");
//        assertThat(all.get(0).getMissionRule().getWeek().isFri()).isTrue();
//        assertThat(all.get(0).getMissionRule().getWeek().isSat()).isTrue();
//    }
}
