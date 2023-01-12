package com.jung.notify.controller;

import com.jung.notify.service.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class KeywordController {

    private final KeywordService keywordService;

    @GetMapping("/keyword")
    public String keyword(@AuthenticationPrincipal User user, Model model){
        model.addAttribute("keywords", keywordService.findAllByMember(user.getUsername()));

        return "keyword/list";
    }
}
