package com.hexcode.pro_clock_out.global.config;

import com.hexcode.pro_clock_out.wolibal.service.WolibalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SchedulerConfig {
    private final WolibalService wolibalService;

    @Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Seoul")
    public void scheduleCreateAutoWolibal() {
        wolibalService.createDailyWolibal();
        log.info("Scheduled task completed");
    }
}