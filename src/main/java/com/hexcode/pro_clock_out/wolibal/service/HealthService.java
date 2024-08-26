package com.hexcode.pro_clock_out.wolibal.service;

import com.hexcode.pro_clock_out.global.domain.Label;
import com.hexcode.pro_clock_out.global.service.GlobalService;
import com.hexcode.pro_clock_out.member.domain.Member;
import com.hexcode.pro_clock_out.wolibal.domain.Health;
import com.hexcode.pro_clock_out.wolibal.domain.Health;
import com.hexcode.pro_clock_out.wolibal.domain.Health;
import com.hexcode.pro_clock_out.wolibal.domain.Wolibal;
import com.hexcode.pro_clock_out.wolibal.dto.UpdateHealthRequest;
import com.hexcode.pro_clock_out.wolibal.exception.HealthNotFoundException;
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
        wolibal.setHealth(health);
    }

    public Health findHealthById(Long healthId) {
        return healthRepository.findById(healthId)
                .orElseThrow(() -> new HealthNotFoundException(healthId));
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

    public Wolibal updateHealthByData(Long healthId, UpdateHealthRequest dto) {
        Health health = findHealthById(healthId);

        health.setCardioFrequency(dto.getCardioFrequency());
        health.setCardioTime(dto.getCardioTime());
        health.setStrengthFrequency(dto.getStrengthFrequency());
        health.setStrengthTime(dto.getStrengthTime());
        health.setSatisfaction(dto.getHealthSatisfaction());

        health.setScore(generateHealthScore(health));
        healthRepository.save(health);

        return health.getWolibal();
    }

    /**
     * 건강 점수 계산 ///////////////////////////////////////////////////
     */
    private int generateHealthScore(Health health) {
        double score1 = calculateHealthScore1(health.getCardioFrequency(), health.getCardioTime()); // 유산소 운동 점수
        double score2 = calculateHealthScore2(health.getStrengthFrequency(), health.getStrengthTime()); // 근력 운동 점수
        double score3 = calculateHealthScore3(health.getDietQuality());
        double basicScore = (score1 + score2 + score3) / 3;
        return globalService.applySatisfaction(basicScore, health.getSatisfaction(), Label.HEALTH);
    }

    // 유산소 운동 점수
    private double calculateHealthScore1(int cardioFrequency, double cardioTime) {
        double frequencyScore = calculateFrequencyScore(cardioFrequency, 3, 5, 80);
        double timeScore = calculateTimeScore(cardioTime, 0.5, 1, 50);
        return (frequencyScore + timeScore) / 2;
    }

    // 근력 운동 점수
    private double calculateHealthScore2(int strengthFrequency, double strengthTime) {
        double frequencyScore = calculateFrequencyScore(strengthFrequency, 2, 4, 70);
        double timeScore = calculateTimeScore(strengthTime, 0.75, 1.25, 70);
        return (frequencyScore + timeScore) / 2;
    }

    // 운동 횟수 점수 계산
    private double calculateFrequencyScore(double frequency, double minFrequency, double maxFrequency, double limit) {
        log.info("frequency: {}", frequency);
        if (frequency <= minFrequency) {
            return 100 / minFrequency * frequency;
        } else if (frequency <= maxFrequency) {
            return 100;
        } else if (frequency <= 7) {
            return ((limit - 100) / (7 - maxFrequency) * frequency) + ((100 * (7 - maxFrequency) - maxFrequency * (limit - 100)) / (7 - maxFrequency));
        }
        return 0;
    }

    // 운동 시간 점수 계산
    private double calculateTimeScore(double time, double minTime, double maxTime, double limit) {
        log.info("time: {}", time);
        if (time < minTime) {
            return 100 / minTime * time;
        } else if (time <= maxTime) {
            return 100;
        } else if (time <= 2) {
            return ((limit - 100) / (7 - maxTime) * time) + ((100 * (7 - maxTime) - maxTime * (limit - 100)) / (7 - maxTime));
        }
        return 0;
    }

    // 균형 잡힌 식사 점수
    private double calculateHealthScore3(int dietQuality) {
        if (dietQuality < 1 || dietQuality > 9) {
            log.info("dietQuality: {}", dietQuality);
            throw new IllegalArgumentException("dietQuality 는 1 이상 9 이하의 정수여야 합니다.");
        }
        return (double) (dietQuality - 1) / 8 * 100;
    }
}
