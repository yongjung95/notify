package com.jung.notify.controller;

import com.jung.notify.domain.Member;
import com.jung.notify.domain.MemberRole;
import com.jung.notify.dto.MemberDto;
import com.jung.notify.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberRestController {

    private final MemberService memberService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping(value = "/member/save")
    public String create(MemberDto.SaveMember saveMember) {

        Member member = Member.builder()
                .id(saveMember.getId())
                .passwd(bCryptPasswordEncoder.encode(saveMember.getPasswd()))
                .memberRole(MemberRole.MEMBER)
                .lineToken(saveMember.getLineToken())
                .created(LocalDateTime.now())
                .build();

        memberService.saveMember(member);
        return "redirect:/";
    }
}
