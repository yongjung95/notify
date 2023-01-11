package com.jung.notify.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
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

    @Data
    public static class StockManageRequest {
        private Long stockId;
    }

    @Data
    @Builder
    public static class StockSearchInfo {
        private String corpName; // 주식 명
        private String price; // 현재 가격
        private String previousPrice; // 전일 종가
        private String openPrice; // 시가
        private String compareDayPoint; // 전일 대비 포인트
        private String compareDayPercent; // 전일 대비 퍼센트
    }

    @Data
    public static class SelectStockManageMember {
        private String corpName;
        private String stockCode;
        private String lineToken;
        @QueryProjection
        public SelectStockManageMember(String corpName, String stockCode, String lineToken) {
            this.corpName = corpName;
            this.stockCode = stockCode;
            this.lineToken = lineToken;
        }
    }
}
