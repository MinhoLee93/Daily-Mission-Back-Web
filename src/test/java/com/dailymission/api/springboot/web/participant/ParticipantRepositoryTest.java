package com.dailymission.api.springboot.web.participant;

import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

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
public class ParticipantRepositoryTest {
    /**
     * [ 2020-03-22 : 이민호 ]
     * 현재까지 custom query 가 존재하지 않으므로 패스한다.
     * */
}
