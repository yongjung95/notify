package com.jung.notify.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

public class WorldStockDto {

    @Data
    public static class SelectWorldStock {
        private Long id;
        private String symbol;
        private String exchange;
        private String englishName;
        private String koreanName;

        @QueryProjection
        public SelectWorldStock(Long id, String symbol, String exchange, String englishName, String koreanName) {
            this.id = id;
            this.symbol = symbol;
            this.exchange = exchange;
            this.englishName = englishName;
            this.koreanName = koreanName;
        }
    }

    @Data
    public static class SelectWorldStockRequest {
        private String koreanName = "";
        private int page = 0;
        private int size = 10;
    }
}
