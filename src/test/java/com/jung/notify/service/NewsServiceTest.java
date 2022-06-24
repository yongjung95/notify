package com.jung.notify.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NewsServiceTest {

    @Autowired
    private NewsService newsService;

    @Autowired
    private UtilService utilService;


    @Test
    public void 네이버_뉴스_API(){
        System.out.println(newsService.news("삼성전자"));
    }

    @Test
    public void 단축_URL(){
        System.out.println(utilService.shortUrl("https://n.news.naver.com/mnews/article/003/0011265263?sid=101"));
    }

}