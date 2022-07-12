package com.jung.notify.api;

import com.jung.notify.api.response.error.ErrorCode;
import com.jung.notify.api.response.model.SingleResult;
import com.jung.notify.api.response.service.ResponseService;
import com.jung.notify.common.StringUtil;
import com.jung.notify.domain.Member;
import com.jung.notify.domain.News;
import com.jung.notify.service.MemberService;
import com.jung.notify.service.MessageService;
import com.jung.notify.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MessageApiController {

    private final MessageService messageService;

    private final MemberService memberService;

    private final NewsService newsService;

    private final ResponseService responseService;


    @PostMapping("/v1/message")
    public SingleResult<?> message(@AuthenticationPrincipal User user){
        Optional<Member> member = memberService.findMemberById(user.getUsername());

        if(StringUtil.isNullOrEmpty(member.get().getLineToken())){
            return responseService.getFailResult(ErrorCode.LINE_TOKEN_IS_NOT_FOUND);
        }

        String keyword = "삼성전자";

        List<News> newsList = newsService.news(keyword);

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[메시지 발송 테스트] 오늘의 " +"\" "+keyword +" \""  + "뉴스 \n\n");

        for (News news : newsList){
            stringBuffer.append(newsList.indexOf(news) + 1).append(". ").append(news.getTitle()).append("\n").append(news.getLink()).append("\n").append(news.getPubDate()).append("\n\n");
        }

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("message", stringBuffer.toString());

        messageService.sendMessage(body, member.get());

        return responseService.getSuccessResult();
    }

}
