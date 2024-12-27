package com.karnty.training.backend.schedule;

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class UserSchedule {
    // 1 => second, 2 => minute, 3 => hour
    // , 4 => day, 5 => month
    // , 6 => year

    /**
     * Every minute
     */
    @Scheduled(cron = "0 * * * * *",zone = "Asia/Bangkok")
    public void testEveryMinute() {
        log.info("Hello, What's up?");
    }
    /**
     * Every Day at 00:00:00
     */
    @Scheduled(cron = "0 0 0 * * *",zone = "Asia/Bangkok")
    public void testEveryMidnight() {

    }
    /**
     * Every Day at 10:50:00
     */
    @Scheduled(cron = "0 50 10 * * *", zone = "Asia/Bangkok")
    public void testEveryDayNightAM() {
        log.info("Hey Hoo!!!");
    }
}
