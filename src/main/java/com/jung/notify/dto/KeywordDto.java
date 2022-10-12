package com.jung.notify.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class KeywordDto {

    @Data
    public static class SaveKeywordDto{
        private String keyword;
    }

    @Data
    public static class RemoveKeywordDto{
        private List<Long> keywordId = new ArrayList<>();
    }

    @Data
    public static class SelectKeyword{
        private Long id;
        private String keyword;
        private String memberId;
    }
}
