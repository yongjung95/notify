package com.jung.notify.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class KeywordDto {

    @Data
    @Builder
    public static class SaveKeywordDto {
        @NotBlank(message = "키워드를 입력해주세요.")
        private String keyword;
        private String memberId;
    }

    @Data
    public static class RemoveKeywordDto {
        @Size(min = 1, message = "키워드를 선택해주세요.")
        private List<Long> keywordId = new ArrayList<>();
    }

    @Data
    public static class SelectKeyword {
        private Long id;
        private String keyword;
        private String memberId;
    }
}
