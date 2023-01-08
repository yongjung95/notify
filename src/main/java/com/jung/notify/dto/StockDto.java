package com.jung.notify.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

public class StockDto {

    @Data
    public static class SelectStock {
        private Long id;
        private String corpCode;
        private String corpName;
        private String stockCode;

        private Long stockManageId;

        private boolean isUse;

        @QueryProjection
        public SelectStock(Long id, String corpCode, String corpName, String stockCode, Long stockManageId, boolean isUse) {
            this.id = id;
            this.corpCode = corpCode;
            this.corpName = corpName;
            this.stockCode = stockCode;
            this.stockManageId = stockManageId;
            this.isUse = isUse;
        }
    }

    @Data
    public static class SelectStockRequest {
        private String corpName = "";
        private int page = 0;
        private int size = 10;
    }
}
