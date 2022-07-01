package com.jung.notify.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

public class MemberDto {

    @Data
    public static class SaveMember{
        private String id;

        private String passwd;

        private String lineToken;
    }
}
