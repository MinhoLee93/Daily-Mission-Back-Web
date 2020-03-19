package com.dailymission.api.springboot.web.user;

import com.dailymission.api.springboot.web.repository.user.AuthProvider;
import com.dailymission.api.springboot.web.repository.user.User;
import com.dailymission.api.springboot.web.repository.user.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * [ 2020-03-18 : 이민호 ]
 * - @DataJpaTest 어노테이션을 통해서 Repository 에 대한 Bean 만 등록합니다.
 * - @DataJpaTest 는 기본적으로 메모리 데이터베이스에 대한 테스트를 진행합니다.
 * - @AutoConfigureTestDatabase 어노테이션을 통해서 profile 에 등록된 데이터베이스 정보로 대체할 수 있습니다.
 * - JpaRepository 에서 기본적으로 기본적으로 제공해주는 findById, findByAll, deleteById 등은 테스트를 하지 않습니다.
 * - 주로 커스텀하게 작성한 쿼리 메서드, QueryDSL 등의 커스텀하게 추가된 메서드를 테스트합니다.
 * - setUp() 메서드를 통해서 Entity 를 데이터베이스에 insert 합니다
 * - 실제로 작성된 쿼리가 어떻게 출력되는지 show-sql 옵션을 통해서 확인 합니다.
 * - ORM 은 SQL 을 직접 장성하지 않으니 실제 쿼리가 어떻게 출력되는지 확인하는 습관을 반드시 가져야합니다.
 * */
@RunWith(SpringRunner.class)
@DataJpaTest
//@ActiveProfiles(TestProfile.TEST)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Ignore
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    private final String USER_NAME = "USER_NAME";
    private final String EMAIL = "EMAIL@gmail.com";
    private final String IMAGE_URL = "IMAGE_URL.jpg";
    private final AuthProvider PROVIDER = AuthProvider.google;
    private final String PROVIDER_ID = "PROVIDER_ID";

    @Before
    public void setup() throws Exception  {
        // save user
        user = userRepository.save(User.builder()
                                        .name(USER_NAME)
                                        .email(EMAIL)
                                        .imageUrl(IMAGE_URL)
                                        .provider(PROVIDER)
                                        .providerId(PROVIDER_ID)
                                        .build());
    }


    /**
     * [ 2020-03-18 : 이민호 ]
     * 설명 : USER 저장 후 email 로 USER 검색시 조회가 된다. (Not Null)
     *
     * Query : select user0_.id as id1_4_,
     *                user0_.created_date as created_2_4_,
     *                user0_.modified_date as modified3_4_,
     *                user0_.email as email4_4_,
     *                user0_.email_verified as email_ve5_4_,
     *                user0_.file_extension as file_ext6_4_,
     *                user0_.image_url as image_ur7_4_,
     *                user0_.name as name8_4_,
     *                user0_.original_file_name as original9_4_
     *                , user0_.password as passwor10_4_,
     *                user0_.provider as provide11_4_,
     *                user0_.provider_id as provide12_4_,
     *                user0_.thumbnail_url as thumbna13_4_
     *          from  user user0_
     *          where user0_.email=?
     * */
    @Test
    public void save_user_THEN_find_by_email_is_not_null(){
        // when
        Optional<User> optional = userRepository.findByEmail(EMAIL);
        User user = optional.orElse(null);

        // then
        assertThat(user).isNotNull();
    }

    /**
     * [ 2020-03-18 : 이민호 ]
     * 설명 : USER 저장 후 exist by email true 를 반환한다.
     *
     * Query : select user0_.id as col_0_0_
     *         from   user user0_
     *         where  user0_.email=?
     *         limit  ?
     * */
    @Test
    public void save_user_THEN_exist_by_email_is_true(){
        // when
        boolean exist = userRepository.existsByEmail(EMAIL);

        // then
        assertThat(exist).isTrue();
    }
}
