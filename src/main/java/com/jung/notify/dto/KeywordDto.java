package com.jung.notify.dto;

import com.jung.notify.domain.Keyword;
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
    public static class SelectKeywordDto{
        private Long id;
        private String keyword;

        public SelectKeywordDto(Keyword keyword){
            this.id = keyword.getId();
            this.keyword = keyword.getKeyword();
        }
    }
}
