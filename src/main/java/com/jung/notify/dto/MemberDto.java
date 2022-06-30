package com.jung.notify.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

public class MemberDto {

    @Data
    public static class SaveMember{
        @NotEmpty(message = "회원 아이디는 필수 입니다")
        private String id;

        @NotEmpty(message = "회원 패스워드는 필수 입니다")
        private String passwd;

        private String lineToken;
    }
}
