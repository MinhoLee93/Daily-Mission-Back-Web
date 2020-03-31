# Daily Mission
> 대국민 미션 인증 프로젝트 😎

[1일 1알고리즘](https://okky.kr/article/696425) 온라인 모임을 운영하며, 체계적인 관리의 필요성을 느껴 프로젝트를 진행했습니다.

누구나 본인들만의 미션을 생성하고 참여자들을 모집해 각자의 미션을 진행할 수 있습니다.

# 사용 방법


# Project Structure
> React(SPA) + Spring Boot(API Server) 구조로 개발했으며, 저는 API Server 를 담당했습니다.

프로젝트에 사용할 기술 목록을 사전에 정의하고, 약 5개월간 해당 기술들을 학습 후 [기술 블로그](https://velog.io/@minholee_93/series)에 정리했습니다. 

> 사용한 기술스택은 다음과 같습니다.

• Spring Boot (API Server)   
• Spring Security (Security)    
• Spring Batch (Batch)  
• MariaDB (RDB)  
• JPA & QueryDSL (ORM)   
• OAuth2.0 + JWT (Login)  
• Redis (Cache)   
• JUnit (Test)  
• AWS (Infra)  
• Nginx (Reverse Proxy Server)   
• Rabbit MQ (Message Broker)  
• Jenkins & Codedeploy (CI/CD) 

> ERD & API는 다음과 같이 정리했습니다.

• ErdCloud : https://www.erdcloud.com/d/HcjicwpDs8zmGdCqL  
• GitBook : https://minholee93.gitbook.io/daily-mission/~/settings/share

🔑 프로젝트에서는 API Server / Infra / 기획 / 설계 / 일정관리 등을 담당했습니다.   

# Spring Boot (API Server)
> React(SPA)에서 요청한 데이터를 JSON으로 response 한다.

전체 구조는 다음과 같습니다.

- config  : project configuration을 관리한다.
- exception : custom exception message를 관리한다.  
- security : security, oauth, jwt 관련 기능들을 관리한다.
- util : util 기능들을 관리한다.
- web 
    - controller : API를 관리한다.
    - dto : request/response dto를 관리한다.
    - repository : domain + JPA/QueryDSL를 관리한다.
    - service : domain에 정의한 business logic 호출 순서를 관리한다.

🔑 비즈니스 로직은 service가 아닌 반드시 domain에 작성한다.

```
mission.setCredential()
mission.matchCredential()
mission.isPossibleToParticipate()
mission.updateImage()
mission.isDeletable()
mission.delete()
mission.isEndable()
mission.end()
mission.getWeekDates()
mission.checkMandatory()
mission.getHistories()
mission.getAllParticipantUser()
mission.getParticipantCountNotBanned()
mission.isValidFileExtension()
mission.isValidStartDate()
mission.isValidMission()
...
```

# Spring Security (Security) 
> 

# Spring Batch (Batch)  
> 매일 새벽 3시에 jenkins job이 미인증 사용자들을 강퇴하고, 종료일자가 지난 미션을 종료한다.

<img src="https://image.daily-mission.com/README/batch.png"></img>

🔑  Batch Job의 중복 처리방지를 위해 job parameter를 전달받아 batch job의 멱등성을 유지한다.

```
@Value("#{jobParameters[requestDate]}")
```

# JPA & QueryDSL (ORM)
> 객체 중심 domain 설계 및 반복적인 CRUD 작업을 대체해 비즈니스 로직에 집중한다.

• JPA : 반복적인 CRUD 작업을 대체해 간단히 DB에서 데이터를 조회한다. 

• QueryDSL : Join & Projections 등 JPA로 해결할 수 없는 SQL은 QueryDSL로 작성한다.


전체 구조는 다음과 같습니다.

- Post (Domain Class)
- PostRepository (JPA Interface)
- PostRepostioryCustom (QueryDSL Interface)
- PostRepositoryCustomImpl (QueryDSL Implements Class)

🔑 JPA와 QueryDSL로 구현한 CRUD는 JUnit Test로 반드시 실행되는 SQL을 직접 확인한다. 

```
select *
from mission mission0_
where mission0_.deleted=?
order by (
           select count(participan1_.mission_id)
           from participant participan1_
           where mission0_.id=participan1_.mission_id
         ) desc,
         mission0_.created_date desc
```

# OAuth2.0 + JWT (Login)  



# Redis (Cache)
> Global Cache Server를 사용해 반복적인 메서드의 호출을 차단, API 응답 성능을 높인다. 

전체 구조는 다음과 같습니다.

- @CachePut : key 값의 Cache를 갱신한다.
- @Cacheable : key가 존재할 경우 Cache 된 결과값을 Return 한다. 존재하지 않을 경우 메서드를 실행 후 결과값을 Cache 한다.
- @CacheEvict : key 값의 Cache를 제거한다.
- TTL : Time-To-Live 를 설정해 Cache가 Alive 할 수 있는 최대 시간을 지정한다.

🔑 JUnit Test 에서 Cache가 활성화 될 경우, 정상적으로 Integration Test를 수행할 수 없다. 따라서 application.yml에서 Cache를 disable 한다.

```
spring.cache.type : none
```

# JUnit (Test) 
> Layer 별로 Bean을 최소한으로 등록시켜 테스트 하고자 하는 로직에 집중해 테스트를 수행한다.

전체 구조는 다음과 같습니다.

- Domain 테스트 : domain 객체들은 가장 핵심이며, 이 객체를 사용하는 계층들이 프로젝트에 다양하게 분포되기 때문에 반드시 테스트 코드를 작성한다.

- Repository 테스트 : @DataJpaTest 어노테이션을 통해서 Repository 에 대한 Bean 만 등록한다. 커스텀하게 작성한 쿼리 메서드, QueryDSL 등의  메서드를 테스트한다. ORM 은 SQL 을 직접 작성하지 않으니 반드시 실제 쿼리가 어떻게 출력되는지 확인한다.

- Service 테스트 : 테스트 진행시 중요 관점이 아닌 것들은 Mocking 처리해서 외부 의존성을 줄인다.

- Controller 테스트 : 모든 Bean 을 올리고 테스트를 진행한다. @Transactional 어노테이션을 추가해 테스트 후 DB를 자동으로 RollBack 한다.

🔑 총 96개의 Test Case를 작성했습니다. (mission : 45 / Participant : 8 / Post : 26 / User : 17)

# AWS (Infra) 
> 전체 프로젝트 인프라 구성 및 계정 별 권한을 관리한다.

전체 구조는 다음과 같습니다.

<img src="https://image.daily-mission.com/README/aws.png"></img>

🔑 EC2의 ssh 접근권한은 반드시 본인의 IP 만 허용한다, 또한 사용자별 IAM 계정 및 권한을 부여해 보안성을 강화한다.


# Nginx (Reverse Proxy Server)  
> 클라이언트로부터 전달받은 요청을 어플리케이션 서버에 전달한 뒤, 어플리케이션 서버가 반환한 결과값을 다시 클라이언트에게 전달한다.



# Rabbit MQ (Message Broker)



# Jenkins & Codedeploy (CI/CD) 





