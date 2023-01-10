package com.jung.notify.repository;

import com.jung.notify.domain.Member;
import com.jung.notify.domain.Stock;
import com.jung.notify.domain.StockManage;
import com.jung.notify.dto.StockDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

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
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "corpName"));

        Page<StockDto.SelectStock> allByMember = stockManageRepository.selectStockManageList(pageRequest, member);

        for (StockDto.SelectStock selectStock : allByMember) {
            System.out.println(selectStock);
        }

    }

}