package com.hexcode.pro_clock_out.global.config;

import com.hexcode.pro_clock_out.wolibal.service.WolibalService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfig {
    private final WolibalService wolibalService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduleCreateAutoWolibal() {
        wolibalService.createDailyWolibal();
    }
}