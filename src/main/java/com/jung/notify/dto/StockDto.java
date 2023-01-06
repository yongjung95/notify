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

    @Data
    public static class SelectStockRequest {
        private String corpName = "";
        private int page = 0;
        private int size = 10;
    }
}
