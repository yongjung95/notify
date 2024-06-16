package com.jung.notify.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
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

    @Data
    @Builder
    public static class AmericaStockSearchInfo {
        private String corpName; // 주식 명
        private String price; // 현재 가격
        private String previousPrice; // 전일 종가
        private String openPrice; // 시가
        private String compareDayPoint; // 전일 대비 포인트
        private String compareDayPercent; // 전일 대비 퍼센트
        private String compareDaySign; // 전일 대비 부호 [1 : 상한, 2: 상승, 3: 보합, 4: 하한, 5: 하락]
    }
}
