package com.jung.notify.service;

import com.jung.notify.common.Sha256;
import com.jung.notify.domain.Keyword;
import com.jung.notify.domain.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KeywordServiceTest {

    @Autowired
    KeywordService keywordService;

    @Autowired
    MemberService memberService;

    @Test
    public void 키워드_생성() {
        // given
        Member member = new Member();

        member.setId("yongjung95");
        member.setPasswd(Sha256.encrypt("1234"));
        member.setCreated(LocalDateTime.now());

        memberService.saveMember(member);

        Keyword keyword = Keyword.builder()
                .keyword("삼성전자")
                .member(member)
                .build();
        // when
        keywordService.saveKeyword(keyword);

        // then
        assertEquals(keyword.getKeyword(), keywordService.findOne(keyword.getId()).getKeyword());

    }
}