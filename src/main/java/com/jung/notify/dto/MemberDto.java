package com.jung.notify.dto;

import com.jung.notify.domain.Member;
import lombok.Data;

public class MemberDto {

    @Data
    public static class SaveMember{
        private String id;

        private String passwd;

        private String lineToken;

    }

    public static Member dtoChangeEntity(SaveMember saveMember) {
        return Member.builder()
                .id(saveMember.id)
                .passwd(saveMember.passwd)
                .lineToken(saveMember.lineToken).build();
    }
}
