package com.jung.notify.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import javax.validation.constraints.NotNull;

public class AmericaStockDto {

    @Data
    public static class SelectAmericaStock {
        private Long id;
        private String symbol;
        private String exchange;
        private String englishName;
        private String koreanName;
        private Long americaStockManageId;
        private boolean isUse;

        @QueryProjection
        public SelectAmericaStock(Long id, String symbol, String exchange, String englishName, String koreanName, Long americaStockManageId, boolean isUse) {
            this.id = id;
            this.symbol = symbol;
            this.exchange = exchange;
            this.englishName = englishName;
            this.koreanName = koreanName;
            this.americaStockManageId = americaStockManageId;
            this.isUse = isUse;
        }
    }

    @Data
    public static class SelectAmericaStockRequest {
        private String koreanName = "";
        private int page = 0;
        private int size = 10;
    }

    @Data
    public static class AmericaStockManageRequest {
        @NotNull(message = "종목을 선택해주세요.")
        private Long americaStockId;
    }

    @Data
    public static class SelectAmericaStockManageMember {
        private String koreanName;
        private String symbol;
        private String exchange;
        private String lineToken;

        @QueryProjection
        public SelectAmericaStockManageMember(String koreanName, String symbol, String exchange, String lineToken) {
            this.koreanName = koreanName;
            this.symbol = symbol;
            this.exchange = exchange;
            this.lineToken = lineToken;
        }
    }
}
