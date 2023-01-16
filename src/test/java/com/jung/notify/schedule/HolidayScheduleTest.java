package com.jung.notify.schedule;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class HolidayScheduleTest {

    @Autowired
    private HolidaySchedule holidaySchedule;

    @Test
    public void 공휴일_테스트() throws Exception {
        holidaySchedule.holidaySchedule();
    }
}