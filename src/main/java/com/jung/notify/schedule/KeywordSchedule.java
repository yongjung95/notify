package com.jung.notify.schedule;

import com.jung.notify.service.KeywordService;
import com.jung.notify.service.MemberService;
import com.jung.notify.service.MessageService;
import com.jung.notify.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KeywordSchedule {

    private final MemberService memberService;

    private final KeywordService keywordService;

    private final NewsService newsService;

    private final MessageService messageService;

//    @Scheduled(cron = "0 0 8 1/1 * *")
//    public void keywordSchedule(){
//
//        List<Member> members = memberService.findAllMember();
//
//        for(Member member : members){
//            if(!StringUtil.isNullOrEmpty(member.getLineToken())){
//                List<Keyword> keywords = keywordService.findAllByMember(member);
//
//                for(Keyword keyword : keywords){
//                    List<News> newsList = newsService.news(keyword.getKeyword());
//
//                    StringBuffer stringBuffer = new StringBuffer();
//                    stringBuffer.append("오늘의 " +"\" "+keyword.getKeyword() +" \""  + "뉴스 \n\n");
//
//                    for (News news : newsList){
//                        stringBuffer.append(newsList.indexOf(news) + 1).append(". ").append(news.getTitle()).append("\n").append(news.getLink()).append("\n").append(news.getPubDate()).append("\n\n");
//                    }
//
//                    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//                    body.add("message", stringBuffer.toString());
//
//                    messageService.sendMessage(body, member);
//                }
//            }
//        }
//    }

    @Scheduled(cron = "0 0 8 1/1 * *", zone = "GMT+9:00")
    public void keywordScheduleV2(){
        keywordService.sendKeywordList();
    }
}
