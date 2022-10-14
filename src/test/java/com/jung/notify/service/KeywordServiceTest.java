package com.jung.notify.service;

import com.jung.notify.common.Sha256;
import com.jung.notify.domain.Keyword;
import com.jung.notify.domain.Member;
import com.jung.notify.dto.MemberDto;
import com.jung.notify.mapper.KeywordMapper;
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
public class KeywordServiceTest {

    @Autowired
    KeywordService keywordService;

    @Autowired
    MemberService memberService;

    @Test
    public void 키워드_생성() {
        // given
        MemberDto.SaveMember saveMember = new MemberDto.SaveMember();

        saveMember.setId("wlswjd95");
        saveMember.setPasswd(Sha256.encrypt("1234"));
//        saveMember.setCreated(LocalDateTime.now());

        memberService.saveMember(MemberDto.dtoChangeEntity(saveMember));

        Optional<Member> member = memberService.findMemberById(saveMember.getId());

        Keyword keyword = Keyword.builder()
                .keyword("삼성전자")
                .member(member.get())
                .build();
        // when
        keywordService.saveKeyword(keyword);

        Keyword findKeyword = keywordService.findOne(keyword.getId(), member.get()).get();

        System.out.println(KeywordMapper.INSTANCE.keywordToSelectKeyword(findKeyword));

        // then
        assertEquals(keyword.getKeyword(), KeywordMapper.INSTANCE.keywordToSelectKeyword(findKeyword).getKeyword());

    }
}