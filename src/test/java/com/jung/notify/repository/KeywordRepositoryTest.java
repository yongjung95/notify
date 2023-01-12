package com.jung.notify.repository;

import com.jung.notify.domain.Keyword;
import com.jung.notify.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class KeywordRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private KeywordRepository keywordRepository;

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

        Keyword keyword1 = Keyword.builder()
                .keyword("삼성전자")
                .member(member1)
                .build();
        Keyword keyword2 = Keyword.builder()
                .keyword("애플")
                .member(member1)
                .build();

        em.persist(keyword1);
        em.persist(keyword2);
    }

    @Test
    public void 회원_키워드_목록조회() {
        Member member = memberRepository.findById("member1").get();

        List<Keyword> memberKeywords = keywordRepository.findAllByMember(member);

        for (Keyword memberKeyword : memberKeywords) {
            System.out.println(memberKeyword.getKeyword());
        }
    }

    @Test
    public void 회원_키워드_조회() {
        Member member = memberRepository.findById("member1").get();

        Optional<Keyword> keyword = keywordRepository.findOneByKeyword("삼성전자", member);

        assertThat(keyword.get().getKeyword()).isEqualTo("삼성전자");
    }
}