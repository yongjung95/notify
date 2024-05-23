package com.jung.notify.schedule;

import com.jung.notify.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockSchedule {

    private final StockService stockService;

    @Scheduled(cron = "0 1 9 1/1 * MON-FRI", zone = "GMT+9:00")
    public void morningStockScheduleV1() {
        if (stockService.getStockApiInfo().getOpndYn()) {
            stockService.sendMorningStockPriceList(true);
        }
    }

    @Scheduled(cron = "0 31 15 1/1 * MON-FRI", zone = "GMT+9:00")
    public void eveningScheduleV1() {
        if (stockService.getStockApiInfo().getOpndYn()) {
            stockService.sendMorningStockPriceList(false);
        }
    }
}
