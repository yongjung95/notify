package com.jung.notify.dto;

import com.jung.notify.domain.MemberRole;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class MemberDto {

    @Data
    public static class SaveMember{
        @NotBlank(message = "아이디를 입력해주세요.")
        private String id;
        @NotBlank(message = "패스워드를 입력해주세요.")
        private String passwd;
        private MemberRole memberRole;
        private String lineToken;
        @NotBlank(message = "이메일을 입력해주세요.")
        @Pattern(regexp = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", message = "이메일 형식을 올바르게 입력해주세요.")
        private String email;
    }

    @Data
    public static class UpdateMember {
        private Long uid;
        private String passwd;
        private MemberRole memberRole;
        private String lineToken;
        @Pattern(regexp = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", message = "이메일 형식을 올바르게 입력해주세요.")
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

    @Data
    public static class SearchMemberId {
        @NotBlank(message = "이메일을 입력해주세요.")
        @Pattern(regexp = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", message = "이메일 형식을 올바르게 입력해주세요.")
        private String email;
    }

    @Data
    public static class SearchMemberPw {
        @NotBlank(message = "아이디를 입력해주세요.")
        private String id;
        @NotBlank(message = "이메일을 입력해주세요.")
        @Pattern(regexp = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", message = "이메일 형식을 올바르게 입력해주세요.")
        private String email;
    }
}
