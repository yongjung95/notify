package com.jung.notify.controller;

import com.jung.notify.domain.Member;
import com.jung.notify.domain.MemberRole;
import com.jung.notify.dto.MemberDto;
import com.jung.notify.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.time.LocalDateTime;

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

}
