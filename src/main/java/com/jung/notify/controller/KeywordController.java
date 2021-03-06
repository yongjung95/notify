package com.jung.notify.controller;

import com.jung.notify.domain.Keyword;
import com.jung.notify.domain.Member;
import com.jung.notify.dto.KeywordDto;
import com.jung.notify.service.KeywordService;
import com.jung.notify.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class KeywordController {

    private final KeywordService keywordService;

    private final MemberService memberService;

    @GetMapping("/keyword")
    public String keyword(@AuthenticationPrincipal User user, Model model){
        Optional<Member> member = memberService.findMemberById(user.getUsername());

        List<Keyword> keywords = keywordService.findAllByMember(member.get());

        model.addAttribute("keywords", keywords.stream().map(KeywordDto.SelectKeywordDto::new).collect(Collectors.toList()));

        return "keyword/list";
    }
}
