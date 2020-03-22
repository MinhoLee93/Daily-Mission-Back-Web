package com.dailymission.api.springboot.web.repository.participant;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ParticipantRepositoryCustomImpl implements ParticipantRepositoryCustom {

//    @Autowired
//    EntityManager em;

    private JPAQueryFactory queryFactory;

//    public ParticipantRepositoryCustomImpl(){
//        super(Participant.class);
//        this.queryFactory = new JPAQueryFactory(em);
//    }
}
