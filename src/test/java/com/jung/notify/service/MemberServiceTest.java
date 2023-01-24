package com.jung.notify.service;

import com.jung.notify.domain.MemberRole;
import com.jung.notify.dto.MemberDto;
import com.jung.notify.mapper.MemberMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    EntityManager em;

    @Autowired
    MailService mailService;

    @Test
    public void 회원_생성() {
        // given
        MemberDto.SaveMember saveMember = new MemberDto.SaveMember();

        saveMember.setId("wlswjd95");
        saveMember.setPasswd("1234");
        saveMember.setEmail("wlswjd95@naver.com");
        saveMember.setMemberRole(MemberRole.MEMBER);

        memberService.saveMember(saveMember);

        // when
        MemberDto.SelectMember member = memberService.findMemberById(saveMember.getId());

        System.out.println(member);
        // then
        assertEquals(saveMember.getId(), member.getId());
    }

    @Test
    public void 회원_조회() {
        // given
        MemberDto.SaveMember saveMember = new MemberDto.SaveMember();

        saveMember.setId("wlswjd95");
        saveMember.setEmail("wlswjd95@naver.com");
        saveMember.setPasswd("1234");

        boolean result = memberService.saveMember(saveMember);

        // then
        assertEquals(result, true);
    }

    @Test
    public void 회원_패스워드_찾기() {
        MemberDto.SelectMember selectMember = memberService.findMemberByIdAndEmail("wlswjd95", "wlswjd95@naver.com");

        MemberDto.UpdateMember updateMember = MemberMapper.INSTANCE.selectMemberToUpdateMember(selectMember);

        boolean result = memberService.resetPasswd(updateMember);

        assertTrue(result);
    }

}