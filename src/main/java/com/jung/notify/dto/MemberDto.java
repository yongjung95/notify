package com.jung.notify.dto;

import com.jung.notify.domain.MemberRole;
import lombok.Data;

public class MemberDto {

    @Data
    public static class SaveMember{
        private String id;
        private String passwd;
        private MemberRole memberRole;
        private String lineToken;
        private String email;
    }

    @Data
    public static class UpdateMember {
        private Long uid;
        private String passwd;
        private MemberRole memberRole;
        private String lineToken;
        private String email;
    }

    @Data
    public static class SelectMember {
        private Long uid;
        private String id;
        private String passwd;
        private MemberRole memberRole;
        private String lineToken;
        private String email;
    }
}
