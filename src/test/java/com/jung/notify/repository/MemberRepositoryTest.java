package com.jung.notify.repository;

import com.jung.notify.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
@Transactional
class MemberRepositoryTest {


    @Autowired
    private EntityManager em;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void before() {
        Member member1 = Member.builder().id("member1").build();
        Member member2 = Member.builder().id("member2").build();
        Member member3 = Member.builder().id("member3").build();
        Member member4 = Member.builder().id("member4").build();

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Test
    public void 멤버조회() {
        List<Member> members = memberRepository.findAllMember();

        for (Member member : members) {
            System.out.println("Member: " + member);
        }
    }
}