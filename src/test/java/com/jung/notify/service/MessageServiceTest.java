package com.jung.notify.service;

import com.jung.notify.domain.News;
import com.jung.notify.dto.MemberDto;
import com.jung.notify.mapper.MemberMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageServiceTest {

    @Autowired
    MessageService messageService;

    @Autowired
    MemberService memberService;

    @Autowired
    NewsService newsService;

    @Test
    public void 메시지_전송(){
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("message", "테스트");
        body.add("type","sticker");
        body.add("stickerPackageId","446");
        body.add("stickerId","1988");

//        messageService.sendMessage(body);
    }

    @Test
    public void 뉴스_메시지_전송(){
        //Given
        List<News> newsList = newsService.news("삼성전자");

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("오늘의 뉴스 \n\n");

        for (News news : newsList){
            stringBuffer.append(newsList.indexOf(news) + 1).append(". ").append(news.getTitle()).append("\n").append(news.getLink()).append("\n").append(news.getPubDate()).append("\n\n");
        }

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("message", stringBuffer.toString());

//        messageService.sendMessage(body);
    }

    @Test
    public void 주식_메시지_전송() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("오늘의 주식 시가 \n\n");


        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("message", stringBuffer.toString());

        MemberDto.SelectMember selectMember = memberService.findMemberById("yongjung95").orElseThrow(NullPointerException::new);

        messageService.sendMessage(body, MemberMapper.INSTANCE.selectMemberToMember(selectMember));
    }

}