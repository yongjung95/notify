package com.jung.notify.schedule;

import com.jung.notify.service.AmericaStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AmericaStockSchedule {

    private final AmericaStockService americaStockService;

    @Scheduled(cron = "0 1 9 1/1 * TUE-SAT", zone = "GMT+9:00")
    public void morningAmericaStockScheduleV1() {
        americaStockService.sendAmericaStockPrice(true);
    }

    @Scheduled(cron = "0 31 20 1/1 * MON-FRI", zone = "GMT+9:00")
    public void eveningAmericaScheduleV1() {
        americaStockService.sendAmericaStockPrice(false);
    }
}
