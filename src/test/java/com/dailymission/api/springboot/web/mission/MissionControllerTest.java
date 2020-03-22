package com.dailymission.api.springboot.web.mission;


import com.dailymission.api.springboot.security.UserPrincipal;
import com.dailymission.api.springboot.web.common.MultipartFileSetup;
import com.dailymission.api.springboot.web.dto.user.UserUpdateRequestDto;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.mission.MissionRepository;
import com.dailymission.api.springboot.web.repository.mission.rule.Week;
import com.dailymission.api.springboot.web.repository.user.User;
import com.dailymission.api.springboot.web.repository.user.UserRepository;
import com.dailymission.api.springboot.web.user.UserSetup;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * [ 2020-03-22 : 이민호 ]
 * - 모든 Bean 을 올리고 테스트를 진행하기 때문에 쉽게 테스트 진행 가능합니다.
 * - API 를 테스트할 경우 요청부터 응답까지 전체적인 테스트 진행 가능합니다.
 * - @Transactional 트랜잭션 어노테이션을 추가하면 테스트코드의 데이터베이스 정보가 자동으로 Rollback 됩니다.
 * - 요청에 대한 메서드를 requestSignUp(...)으로 분리해서 재사용성을 높입니다.
 * - 해당 메서드로 validate 를 실패하는 케이스도 작성합니다.
 * - andDo(print()) 메서드를 추가해서 해당 요청에 대한 출력을 확인합니다. 디버깅에 매우 유용합니다.
 * - 모든 response 에 대한 andExpect 를 작성합니다.
 * - response 에 하나라도 빠지거나 변경되면 API 변경이 이루어진 것이고 그 변경에 맞게 테스트 코드도 변경되어야 합니다.
 * - 회원 조회 테스트 강은 경우 memberSetup.save(); 메서드로 테스트전에 데이터베이스에 insert 합니다.
 * - 데이터베이스에 미리 있는 값을 검증하는 것은 데이터베이스 상태에 의존한 코드가 되며 누군가가 회원 정보를 변경하게 되면 테스트 코드가 실패하게 됩니다.
 * - 테스트 전에 데이터를 insert 하지 않는다면 테스트 코드 구동 전에 .sql 으로 미리 데이터베이스를 준비시킵니다.
 * - 중요한 것은 데이터베이스 상태에 너무 의존적인 테스트는 향후 로직의 문제가 없더라도 테스트가 실패하는 상황이 자주 만나게 됩니다.
 * */
@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.config.location=" +
        "classpath:/application.yml" +
        ",classpath:/application-oauth.yml" +
        ",classpath:/aws.yml"
)
@Transactional
public class MissionControllerTest {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private UserRepository userRepository;

    private Mission mission;
    private Week week;
    private User user;
    private MockMvc mvc;

    @Before
    public void setup(){

        // mvc
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        // save user
        user = userRepository.save(UserSetup.builder().build().get());

        // save mission
        mission = missionRepository.save(MissionSetup.builder().user(user).build().get());

        // week
        week = mission.getMissionRule().getWeek();
    }

    /**
     * [ 2020-03-12 : 이민호 ]
     * 설명 : 미션의 디테일 정보와 참여중인 사용자들의 이름/사진/강퇴여부를 가져온다
     *        {"id":62,
     *         "week": {"sun":true,"mon":true,"tue":true,"wed":true,"thu":true,"fri":false,"sat":false},
     *         "userName":"USER_NAME",
     *         "title":"TITLE",
     *         "content":"MISSION_CONTENT",
     *         "thumbnailUrlDetail":"IMAGE_URL.jpg",
     *         "participants":[],
     *         "startDate":"2020-04-01",
     *         "endDate":"2020-04-28",
     *         "ended":false
     *        }
     * */
    @Test
    public void get_mission_detail_success() throws Exception {
        // given
        final UserPrincipal userPrincipal = UserPrincipal.create(user);
        Long id = mission.getId();

        // when
        final ResultActions resultActions = getRequest("/api/mission/" + id, userPrincipal);

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(mission.getId()))
                .andExpect(jsonPath("week.sun").value(week.isSun()))
                .andExpect(jsonPath("week.mon").value(week.isMon()))
                .andExpect(jsonPath("week.tue").value(week.isTue()))
                .andExpect(jsonPath("week.wed").value(week.isWed()))
                .andExpect(jsonPath("week.thu").value(week.isThu()))
                .andExpect(jsonPath("week.fri").value(week.isFri()))
                .andExpect(jsonPath("week.sat").value(week.isSat()))
                .andExpect(jsonPath("userName").value(user.getName()))
                .andExpect(jsonPath("title").value(mission.getTitle()))
                .andExpect(jsonPath("content").value(mission.getContent()))
                .andExpect(jsonPath("thumbnailUrlDetail").value(mission.getThumbnailUrlDetail()))
                .andExpect(jsonPath("participants").isEmpty())
                .andExpect(jsonPath("startDate").value(mission.getStartDate().toString()))
                .andExpect(jsonPath("endDate").value(mission.getEndDate().toString()))
                .andExpect(jsonPath("ended").value(mission.isEnded()));
    }


    /**
     * [ 2020-03-22 : 이민호 ]
     * 설명 : 미션을 삭제한다.
     *        삭제 요청은 방장만 할 수 있다.
     *        미션 시작전 & 참여한 사용자가 없을 경우에만 삭제 가능하다.
     *        {
     *         "id":69
     *        }
     * */
    @Test
    public void delete_mission_success() throws Exception {
        // given
        final UserPrincipal userPrincipal = UserPrincipal.create(user);
        Long id = mission.getId();

        // when
        final ResultActions resultActions = deleteRequest("/api/mission/delete/" + id, userPrincipal);

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(mission.getId()));
    }


    /**
     * [ 2020-03-22 : 이민호 ]
     * 설명 : 신규 미션 목록을 가져온다.
     *     [
     *     {"id":60,
     *      "title":"주말마다 영화보기",
     *      "content":"영화 같이 보실분?",
     *      "week":{"sun":true,"mon":false,"tue":false,"wed":false,"thu":false,"fri":true,"sat":true},
     *      "thumbnailUrlNew":"https://image.daily-mission.com.s3.ap-northeast-2.amazonaws.com/%EC%A3%BC%EB%A7%90%EB%A7%88%EB%8B%A4%20%EC%98%81%ED%99%94%EB%B3%B4%EA%B8%B0/202003201637_unnamed_300_350.png",
     *      "userName":"수박",
     *      "userThumbnailUrl":"https://image.daily-mission.com.s3.ap-northeast-2.amazonaws.com/google/2/202003161641_56552157-%EC%88%98%EB%B0%95-%EC%A1%B0%EA%B0%81%EC%9E%85%EB%8B%88%EB%8B%A4-%EB%B2%A1%ED%84%B0-%EC%9D%BC%EB%9F%AC%EC%8A%A4%ED%8A%B8-%EB%A0%88%EC%9D%B4-%EC%85%98-_40_40.jpg",
     *      "userCount":0,
     *      "startDate":"2020-03-20",
     *      "endDate":"2020-03-31",
     *      "ended":true
     *     },
     *     ..]
     * */
    @Test
    public void get_mission_new_list_success() throws Exception {

        // when
        final ResultActions resultActions = getRequest("/api/mission/new");

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0]id").value(mission.getId()))
                .andExpect(jsonPath("[0]week.sun").value(week.isSun()))
                .andExpect(jsonPath("[0]week.mon").value(week.isMon()))
                .andExpect(jsonPath("[0]week.tue").value(week.isTue()))
                .andExpect(jsonPath("[0]week.wed").value(week.isWed()))
                .andExpect(jsonPath("[0]week.thu").value(week.isThu()))
                .andExpect(jsonPath("[0]week.fri").value(week.isFri()))
                .andExpect(jsonPath("[0]week.sat").value(week.isSat()))
                .andExpect(jsonPath("[0]userName").value(user.getName()))
                .andExpect(jsonPath("[0]title").value(mission.getTitle()))
                .andExpect(jsonPath("[0]content").value(mission.getContent()))
                .andExpect(jsonPath("[0]thumbnailUrlNew").value(mission.getThumbnailUrlNew()))
                .andExpect(jsonPath("[0]userName").value(user.getName()))
                .andExpect(jsonPath("[0]userThumbnailUrl").value(user.getThumbnailUrl()))
                // REST API 를 사용해 USER 를 저장하면, 생성자는 바로 참여하지만, DB로 바로 Insert 하면 참여자는 0명이다.
                .andExpect(jsonPath("[0]userCount").value(0))
                .andExpect(jsonPath("[0]startDate").value(mission.getStartDate().toString()))
                .andExpect(jsonPath("[0]endDate").value(mission.getEndDate().toString()))
                .andExpect(jsonPath("[0]ended").value(mission.isEnded()));
    }

    /**
     * [ 2020-03-22 : 이민호 ]
     * 설명 : 전체 미션 목록을 가져온다.
     *     [
     *     {"id":60,
     *      "title":"주말마다 영화보기",
     *      "content":"영화 같이 보실분?",
     *      "week":{"sun":true,"mon":false,"tue":false,"wed":false,"thu":false,"fri":true,"sat":true},
     *      "thumbnailUrlAll":"https://image.daily-mission.com.s3.ap-northeast-2.amazonaws.com/%EC%A3%BC%EB%A7%90%EB%A7%88%EB%8B%A4%20%EC%98%81%ED%99%94%EB%B3%B4%EA%B8%B0/202003201637_unnamed_300_350.png",
     *      "userName":"수박",
     *      "userThumbnailUrl":"https://image.daily-mission.com.s3.ap-northeast-2.amazonaws.com/google/2/202003161641_56552157-%EC%88%98%EB%B0%95-%EC%A1%B0%EA%B0%81%EC%9E%85%EB%8B%88%EB%8B%A4-%EB%B2%A1%ED%84%B0-%EC%9D%BC%EB%9F%AC%EC%8A%A4%ED%8A%B8-%EB%A0%88%EC%9D%B4-%EC%85%98-_40_40.jpg",
     *      "userCount":0,
     *      "startDate":"2020-03-20",
     *      "endDate":"2020-03-31",
     *      "ended":true
     *     },
     *     ..]
     * */
    @Test
    public void get_mission_all_list_success() throws Exception {

        // when
        final ResultActions resultActions = getRequest("/api/mission/all");

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0]id").value(mission.getId()))
                .andExpect(jsonPath("[0]week.sun").value(week.isSun()))
                .andExpect(jsonPath("[0]week.mon").value(week.isMon()))
                .andExpect(jsonPath("[0]week.tue").value(week.isTue()))
                .andExpect(jsonPath("[0]week.wed").value(week.isWed()))
                .andExpect(jsonPath("[0]week.thu").value(week.isThu()))
                .andExpect(jsonPath("[0]week.fri").value(week.isFri()))
                .andExpect(jsonPath("[0]week.sat").value(week.isSat()))
                .andExpect(jsonPath("[0]userName").value(user.getName()))
                .andExpect(jsonPath("[0]title").value(mission.getTitle()))
                .andExpect(jsonPath("[0]content").value(mission.getContent()))
                .andExpect(jsonPath("[0]thumbnailUrlAll").value(mission.getThumbnailUrlAll()))
                .andExpect(jsonPath("[0]userName").value(user.getName()))
                .andExpect(jsonPath("[0]userThumbnailUrl").value(user.getThumbnailUrl()))
                // REST API 를 사용해 USER 를 저장하면, 생성자는 바로 참여하지만, DB로 바로 Insert 하면 참여자는 0명이다.
                .andExpect(jsonPath("[0]userCount").value(0))
                .andExpect(jsonPath("[0]startDate").value(mission.getStartDate().toString()))
                .andExpect(jsonPath("[0]endDate").value(mission.getEndDate().toString()))
                .andExpect(jsonPath("[0]ended").value(mission.isEnded()));
    }

    /**
     * [ 2020-03-22 : 이민호 ]
     * 설명 : HOT 미션 목록을 가져온다.
     *     [
     *     {"id":60,
     *      "title":"주말마다 영화보기",
     *      "content":"영화 같이 보실분?",
     *      "week":{"sun":true,"mon":false,"tue":false,"wed":false,"thu":false,"fri":true,"sat":true},
     *      "thumbnailUrlHot":"https://image.daily-mission.com.s3.ap-northeast-2.amazonaws.com/%EC%A3%BC%EB%A7%90%EB%A7%88%EB%8B%A4%20%EC%98%81%ED%99%94%EB%B3%B4%EA%B8%B0/202003201637_unnamed_300_350.png",
     *      "userName":"수박",
     *      "userThumbnailUrl":"https://image.daily-mission.com.s3.ap-northeast-2.amazonaws.com/google/2/202003161641_56552157-%EC%88%98%EB%B0%95-%EC%A1%B0%EA%B0%81%EC%9E%85%EB%8B%88%EB%8B%A4-%EB%B2%A1%ED%84%B0-%EC%9D%BC%EB%9F%AC%EC%8A%A4%ED%8A%B8-%EB%A0%88%EC%9D%B4-%EC%85%98-_40_40.jpg",
     *      "userCount":0,
     *      "startDate":"2020-03-20",
     *      "endDate":"2020-03-31",
     *      "ended":true
     *     },
     *     ..]
     * */
    @Test
    public void get_mission_hot_list_success() throws Exception {

        // when
        final ResultActions resultActions = getRequest("/api/mission/hot");

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("[-1]id").value(mission.getId()));
//                .andExpect(jsonPath("[0]week.sun").value(week.isSun()))
//                .andExpect(jsonPath("[0]week.mon").value(week.isMon()))
//                .andExpect(jsonPath("[0]week.tue").value(week.isTue()))
//                .andExpect(jsonPath("[0]week.wed").value(week.isWed()))
//                .andExpect(jsonPath("[0]week.thu").value(week.isThu()))
//                .andExpect(jsonPath("[0]week.fri").value(week.isFri()))
//                .andExpect(jsonPath("[0]week.sat").value(week.isSat()))
//                .andExpect(jsonPath("[0]userName").value(user.getName()))
//                .andExpect(jsonPath("[0]title").value(mission.getTitle()))
//                .andExpect(jsonPath("[0]content").value(mission.getContent()))
//                .andExpect(jsonPath("[0]thumbnailUrlAll").value(mission.getThumbnailUrlAll()))
//                .andExpect(jsonPath("[0]userName").value(user.getName()))
//                .andExpect(jsonPath("[0]userThumbnailUrl").value(user.getThumbnailUrl()))
//                // REST API 를 사용해 USER 를 저장하면, 생성자는 바로 참여하지만, DB로 바로 Insert 하면 참여자는 0명이다.
//                .andExpect(jsonPath("[0]userCount").value(0))
//                .andExpect(jsonPath("[0]startDate").value(mission.getStartDate().toString()))
//                .andExpect(jsonPath("[0]endDate").value(mission.getEndDate().toString()))
//                .andExpect(jsonPath("[0]ended").value(mission.isEnded()));
    }

    /**
     * [ 2020-03-22 : 이민호 ]
     * 설명 : getRequest 를 수행한다.
     *       api 에 호출할 GET Rest API 를 입력한다. ex) api = "/api/mission/51"
     * */
    private ResultActions getRequest(String api) throws Exception {
        return  mvc.perform(
                get(api)
                )
                .andDo(print());
    }


    /**
     * [ 2020-03-22 : 이민호 ]
     * 설명 : getRequest 를 수행한다.
     *       UserDetails 를 with(user(***)) 에 넘겨, Authenticated User 를 사용한다.
     *       api 에 호출할 GET Rest API 를 입력한다. ex) api = "/api/mission/51"
     * */
    private ResultActions getRequest(String api, UserPrincipal userPrincipal) throws Exception {
        return  mvc.perform(
                get(api)
                        .with(user(userPrincipal)))
                .andDo(print());
    }

    /**
     * [ 2020-03-22 : 이민호 ]
     * 설명 : deleteRequest 를 수행한다.
     *        UserDetails 를 with(user(***)) 에 넘겨, Authenticated User 를 사용한다.
     *        api 에 호출할 DELETE Rest API 를 입력한다. ex) api = "/api/mission/delete/51"
     * */
    private ResultActions deleteRequest(String api, UserPrincipal userPrincipal) throws Exception {
        return  mvc.perform(
                delete(api)
                        .with(user(userPrincipal)))
                .andDo(print());
    }

    /**
     * [ 2020-03-19 : 이민호 ]
     * 설명 : postRequest 를 수행한다.
     *       UserDetails 를 with(user(***)) 에 넘겨, Authenticated User 를 사용한다.
     *       MockMultipartFile 을 생성해 multipart 의 file 에 넘긴다.
     *       multipart/form-data 는 requestBody JSON 이 아닌 param 으로 변수를 받는다.
     * */
    private ResultActions postRequest(UserUpdateRequestDto requestDto, UserPrincipal userPrincipal) throws Exception {
        final MockMultipartFile file = MultipartFileSetup.builder().build().get();

        return  mvc.perform(
                multipart("/user/me/update")
                        .file(file)
                        .param("id", requestDto.getId().toString())
                        .param("userName",requestDto.getUserName())
                        .with(user(userPrincipal)))
                .andDo(print());
    }

    /**
     * [ 2020-03-19 : 이민호 ]
     * 설명 : object 를 Json 으로 convert 한다.
     *
     * */
    private String asJsonString(final Object obj){
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
