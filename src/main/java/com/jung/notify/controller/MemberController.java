package com.jung.notify.controller;

import com.jung.notify.dto.MemberDto;
import com.jung.notify.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/member/new")
    public String createForm(Model model){
        model.addAttribute("saveMember", new MemberDto.SaveMember());

        return "member/createMemberForm";
    }

    @GetMapping("/member")
    public String member(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("member", memberService.findMemberById(user.getUsername()));

        return "member/myPage";
    }

}
