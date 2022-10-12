package com.jung.notify.mapper;

import com.jung.notify.domain.Member;
import com.jung.notify.dto.MemberDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberMapperTest {

    @Test
    public void Member_Mapper_MemberDTO() {
        Member member = Member.builder()
                .id("wlswjd95")
                .passwd("SSSSS")
                .build();

        MemberDto.SelectMember selectMember = MemberMapper.INSTANCE.memberToSelectMember(member);

        System.out.println(selectMember);
    }
}