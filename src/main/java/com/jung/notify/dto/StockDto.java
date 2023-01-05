package com.jung.notify.dto;

import lombok.Data;

public class StockDto {

    @Data
    public static class SelectStock {
        private Long id;
        private String corpCode;
        private String corpName;
        private String stockCode;
    }
}
