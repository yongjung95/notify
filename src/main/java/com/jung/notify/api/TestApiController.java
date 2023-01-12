package com.jung.notify.api;

import com.jung.notify.api.response.error.ErrorCode;
import com.jung.notify.api.response.model.SingleResult;
import com.jung.notify.api.response.service.ResponseService;
import com.jung.notify.common.StringUtil;
import com.jung.notify.domain.News;
import com.jung.notify.dto.MemberDto;
import com.jung.notify.mapper.MemberMapper;
import com.jung.notify.service.MemberService;
import com.jung.notify.service.MessageService;
import com.jung.notify.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestApiController {

    private final MessageService messageService;

    private final MemberService memberService;

    private final NewsService newsService;

    private final ResponseService responseService;

    @GetMapping("/v1/test/keyword")
    public SingleResult<?> keywordTest(@AuthenticationPrincipal User user){
        MemberDto.SelectMember selectMember = memberService.findMemberById(user.getUsername()).orElseThrow(NullPointerException::new);

        if(StringUtil.isNullOrEmpty(selectMember.getLineToken())){
            return responseService.getFailResult(ErrorCode.LINE_TOKEN_IS_NOT_FOUND);
        }

        List<News> newsList = new ArrayList<>();
        int start = 0;

        while (newsList.size() < 10){
            List<News> resultList = newsService.dateNews("애플", start += 10);

            for (News news : resultList) {
                if(news.getTitle().contains("애플") && newsList.size() < 10){
                    newsList.add(news);
                }
            }
        }

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("오늘의 " +"\" "+ "애플" +" \""  + "뉴스 \n\n");

        for (News news : newsList){
            stringBuffer.append(newsList.indexOf(news) + 1).append(". ").append(news.getTitle()).append("\n").append(news.getLink()).append("\n").append(news.getPubDate()).append("\n\n");
        }

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("message", stringBuffer.toString());

        messageService.sendMessage(body, MemberMapper.INSTANCE.selectMemberToMember(selectMember));

        return responseService.getSuccessResult();
    }

    @GetMapping("/v1/test/keywords")
    public SingleResult<?> keywordsTest(@AuthenticationPrincipal User user){
        MemberDto.SelectMember selectMember = memberService.findMemberById(user.getUsername()).orElseThrow(NullPointerException::new);

        if(StringUtil.isNullOrEmpty(selectMember.getLineToken())){
            return responseService.getFailResult(ErrorCode.LINE_TOKEN_IS_NOT_FOUND);
        }


        List<String> keywords = new ArrayList<>();

//        keywords.add("삼성전자");
        keywords.add("javascript");

        for (String keyword : keywords) {
            List<News> newsList = new ArrayList<>();

            int start = 0;

            while (newsList.size() < 10 && start < 1000){
                System.out.println(start + " 번째");
                List<News> resultList = newsService.dateNews(keyword, start += 10);

                for (News news : resultList) {
                    if(news.getTitle().contains(keyword) && newsList.size() < 10){
                        System.out.println(resultList);
                        newsList.add(news);
                    }
                }
            }

            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("오늘의 " +"\""+ keyword +"\""  + " 뉴스 \n\n");

            for (News news : newsList){
                stringBuffer.append(newsList.indexOf(news) + 1).append(". ").append(news.getTitle()).append("\n").append(news.getLink()).append("\n").append(news.getPubDate()).append("\n\n");
            }

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("message", stringBuffer.toString());

            messageService.sendMessage(body, MemberMapper.INSTANCE.selectMemberToMember(selectMember));
        }


        return responseService.getSuccessResult();
    }
}
