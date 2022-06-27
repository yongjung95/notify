package com.jung.notify.service;

import com.jung.notify.common.Sha256;
import com.jung.notify.dto.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    public void 회원_생성() {
        // given
        Member member = new Member();

        member.setId("yongjung95");
        member.setPasswd(Sha256.encrypt("1234"));
        member.setCreated(LocalDateTime.now());

        // when
        Long uid = memberService.saveMember(member);

        // then
        assertEquals(uid, member.getUid());
    }

    @Test
    public void 회원_조회() {
        // given
        Member member = new Member();

        member.setId("yongjung95");
        member.setPasswd(Sha256.encrypt("1234"));
        member.setCreated(LocalDateTime.now());

        Long uid = memberService.saveMember(member);

        // when
        Member findMember = memberService.findMemberByUid(1L);

        // then
        assertEquals(uid, findMember.getUid());
    }

}