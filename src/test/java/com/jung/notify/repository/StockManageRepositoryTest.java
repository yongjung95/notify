package com.jung.notify.repository;

import com.jung.notify.domain.Member;
import com.jung.notify.domain.Stock;
import com.jung.notify.domain.StockManage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@TestPropertySource(locations="classpath:application.properties")
@SpringBootTest
@Transactional
@Rollback(value = false)
class StockManageRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private StockManageRepository stockManageRepository;

    @Autowired
    private StockRepository stockRepository;

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

        Stock stock1 = Stock.builder()
                .corpCode("111111")
                .corpName("삼성전자")
                .stockCode("111111")
                .build();

        Stock stock2 = Stock.builder()
                .corpCode("222222")
                .corpName("LG전자")
                .stockCode("222222")
                .build();

        em.persist(stock1);
        em.persist(stock2);
    }

    @Test
    public void 회원_주식_등록_조회() {
        Stock stock = stockRepository.findById(5L).get();
        Member member = memberRepository.findById("member1").get();

        StockManage stockManage = StockManage.builder()
                .stock(stock)
                .member(member)
                .isUse(false)
                .build();

        stockManageRepository.save(stockManage);

        List<StockManage> allByMember = stockManageRepository.findAllByMember(member);

        for (StockManage manage : allByMember) {
            System.out.println(manage);
        }

    }

}