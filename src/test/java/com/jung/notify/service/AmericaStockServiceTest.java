package com.jung.notify.service;

import com.jung.notify.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;

@Slf4j
@SpringBootTest
class AmericaStockServiceTest {


    @Autowired
    AmericaStockService americaStockService;

    @Autowired
    EntityManager em;

    @Test
    void 미국주식_관심등록_테스트() throws Exception {
        //given
        Member member1 = Member.builder().id("member1").build();
        em.persist(member1);

        //when
        americaStockService.saveAmericaStockManage(1L, member1.getId());

        //then

    }

    @Test
    void 미국주식_조회_테스트() {
        log.info(americaStockService.getStockInfo("AAPL", "NAS").toString());
    }

    @Test
    void 미국주식_메시지_전송_테스트() {
        americaStockService.sendAmericaStockPrice(true);
    }
}