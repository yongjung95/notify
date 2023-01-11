package com.jung.notify.schedule;

import com.jung.notify.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockSchedule {

    private final StockService stockService;

    @Scheduled(cron = "0 1 9 1/1 * *")
    public void morningStockScheduleV1() {
        stockService.sendMorningStockPriceList(true);
    }

    @Scheduled(cron = "0 55 15 1/1 * *")
    public void eveningScheduleV1() {
        stockService.sendMorningStockPriceList(false);
    }
}
