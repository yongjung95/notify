package com.jung.notify.service;

import com.jung.notify.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

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
}