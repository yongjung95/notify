package com.jung.notify.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdviceServiceTest {

    @Autowired
    AdviceService adviceService;

    @Test
    public void 명언조회(){
        System.out.println(adviceService.advice());
    }

}