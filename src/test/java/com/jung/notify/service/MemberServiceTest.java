package com.jung.notify.service;

import com.jung.notify.common.Sha256;
import com.jung.notify.domain.Member;
import com.jung.notify.domain.MemberRole;
import com.jung.notify.dto.MemberDto;
import com.jung.notify.mapper.MemberMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    public void 회원_생성() {
        // given
        MemberDto.SaveMember saveMember = new MemberDto.SaveMember();

        saveMember.setId("yongjung95");
        saveMember.setPasswd(Sha256.encrypt("1234"));
        saveMember.setMemberRole(MemberRole.MEMBER);

        memberService.saveMember(MemberMapper.INSTANCE.saveMemberToMember(saveMember));

        // when
        Optional<Member> member = memberService.findMemberById(saveMember.getId());
        // then
        assertEquals(saveMember.getId(), member.get().getId());
    }

    @Test
    public void 회원_조회() {
        // given
        MemberDto.SaveMember saveMember = new MemberDto.SaveMember();

        saveMember.setId("yongjung95");
        saveMember.setPasswd(Sha256.encrypt("1234"));

        Long uid = memberService.saveMember(MemberDto.dtoChangeEntity(saveMember));

        // when
        Member findMember = memberService.findMemberByUid(1L);

        // then
        assertEquals(uid, findMember.getUid());
    }

}