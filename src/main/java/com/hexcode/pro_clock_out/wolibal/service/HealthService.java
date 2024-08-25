package com.hexcode.pro_clock_out.wolibal.service;

import com.hexcode.pro_clock_out.global.domain.Label;
import com.hexcode.pro_clock_out.global.service.GlobalService;
import com.hexcode.pro_clock_out.wolibal.domain.Health;
import com.hexcode.pro_clock_out.wolibal.domain.Health;
import com.hexcode.pro_clock_out.wolibal.domain.Health;
import com.hexcode.pro_clock_out.wolibal.domain.Wolibal;
import com.hexcode.pro_clock_out.wolibal.exception.HealthNotFoundException;
import com.hexcode.pro_clock_out.wolibal.repository.HealthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class HealthService {
    private final GlobalService globalService;
    private final HealthRepository healthRepository;

    public void initializeHealth(Wolibal wolibal) {
        Health health = Health.builder()
                .wolibal(wolibal)
                .build();
        healthRepository.save(health);
    }

    public Health findHealthByWolibal(Wolibal wolibal) {
        return healthRepository.findByWolibal(wolibal)
                .orElseThrow(() -> new HealthNotFoundException(wolibal));
    }

    public void createAutoHealth(Wolibal yesterdayWolibal, Wolibal todayWolibal) {
        Health yesterdayHealth = findHealthByWolibal(yesterdayWolibal);
        Health todayHealth = Health.builder()
                .score(yesterdayHealth.getScore())
                .cardioFrequency(yesterdayHealth.getCardioFrequency())
                .cardioTime(yesterdayHealth.getCardioTime())
                .strengthFrequency(yesterdayHealth.getStrengthFrequency())
                .strengthTime(yesterdayHealth.getStrengthTime())
                .dietQuality(yesterdayHealth.getDietQuality())
                .satisfaction(yesterdayHealth.getSatisfaction())
                .wolibal(todayWolibal)
                .build();
        healthRepository.save(todayHealth);
    }

    public void updateHealthBySatisfaction(Wolibal wolibal, int satisfaction) {
        Health health = findHealthByWolibal(wolibal);
        health.setSatisfaction(satisfaction);
        health.setScore(globalService.applySatisfaction(health.getScore(), satisfaction, Label.HEALTH));
    }
}
