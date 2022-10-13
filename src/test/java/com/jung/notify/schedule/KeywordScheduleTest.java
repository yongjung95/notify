package com.jung.notify.schedule;

import com.jung.notify.domain.News;
import com.jung.notify.service.NewsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.unbescape.html.HtmlEscape;

import java.util.List;

@SpringBootTest
class KeywordScheduleTest {

    @Autowired
    NewsService newsService;

    @Test
    public void 뉴스_테스트() {
        List<News> javascript = newsService.news("javascript");

        for (News news : javascript) {
            System.out.println(HtmlEscape.unescapeHtml(news.getTitle()));
        }
    }
}