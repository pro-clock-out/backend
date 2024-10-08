package com.hexcode.pro_clock_out.wolibal.service;

import com.hexcode.pro_clock_out.global.domain.Label;
import com.hexcode.pro_clock_out.global.service.GlobalService;
import com.hexcode.pro_clock_out.wolibal.domain.Sleep;
import com.hexcode.pro_clock_out.wolibal.domain.Wolibal;
import com.hexcode.pro_clock_out.wolibal.dto.UpdateSleepRequest;
import com.hexcode.pro_clock_out.wolibal.exception.SleepNotFoundException;
import com.hexcode.pro_clock_out.wolibal.repository.SleepRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SleepService {
    private final GlobalService globalService;
    private final SleepRepository sleepRepository;

    public void initializeSleep(Wolibal wolibal) {
        Sleep sleep = Sleep.builder()
                .wolibal(wolibal)
                .build();
        sleepRepository.save(sleep);
        wolibal.setSleep(sleep);
    }

    public Sleep findSleepById(Long sleepId) {
        return sleepRepository.findById(sleepId)
                .orElseThrow(() -> new SleepNotFoundException(sleepId));
    }

    public Sleep findSleepByWolibal(Wolibal wolibal) {
        return sleepRepository.findByWolibal(wolibal)
                .orElseThrow(() -> new SleepNotFoundException(wolibal));
    }

    public void createAutoSleep(Wolibal yesterdayWolibal, Wolibal todayWolibal) {
        Sleep yesterdaySleep = findSleepByWolibal(yesterdayWolibal);
        Sleep todaySleep = Sleep.builder()
                .score(yesterdaySleep.getScore())
                .workdayBedtime(yesterdaySleep.getWorkdayBedtime())
                .workdayWakeup(yesterdaySleep.getWorkdayWakeup())
                .dayoffBedtime(yesterdaySleep.getDayoffBedtime())
                .dayoffWakeup(yesterdaySleep.getDayoffWakeup())
                .satisfaction(yesterdaySleep.getSatisfaction())
                .wolibal(todayWolibal)
                .build();
        sleepRepository.save(todaySleep);
    }

    public void updateSleepBySatisfaction(Wolibal wolibal, int satisfaction) {
        Sleep sleep = findSleepByWolibal(wolibal);
        sleep.setSatisfaction(satisfaction);
        sleep.setScore(globalService.applySatisfaction(sleep.getScore(), satisfaction, Label.SLEEP));
    }

    public void updateSleepByData(Sleep sleep, UpdateSleepRequest dto) {
        sleep.setWorkdayBedtime(dto.getWorkdayBedtime());
        sleep.setWorkdayWakeup(dto.getWorkdayWakeup());
        sleep.setDayoffBedtime(dto.getDayoffBedtime());
        sleep.setDayoffWakeup(dto.getDayoffWakeup());
        sleep.setSatisfaction(dto.getSleepSatisfaction());

        sleep.setScore(generateSleepScore(sleep));
        sleepRepository.save(sleep);
    }

    public String describeSleepData(Wolibal wolibal) {
        Sleep sleep = findSleepByWolibal(wolibal);
        Double workdayBedtime = sleep.getWorkdayBedtime();
        Double workdayWakeup = sleep.getWorkdayWakeup();
        Double dayoffBedtime = sleep.getDayoffBedtime();
        Double dayoffWakeup = sleep.getDayoffWakeup();
        Integer satisfaction = sleep.getSatisfaction();

        StringBuilder result = new StringBuilder();
        if (workdayBedtime != null && workdayWakeup != null) { result.append(String.format("사용자는 근무일에 %.1f시에 취침하고, %.1f시에 기상한다.\n", workdayBedtime % 24, workdayWakeup % 24)); }
        if (dayoffBedtime != null && dayoffWakeup != null) { result.append(String.format("사용자는 휴무일에 %.1f시에 취침하고, %.1f시에 기상한다.\n", dayoffBedtime % 24, dayoffWakeup % 24)); }
        if (satisfaction != null) { result.append(String.format("사용자의 수면에 대한 워라밸 만족도는 %d점이다. 1일수록 매우 불만족이고, 9일수록 매우 만족이다.\n", satisfaction)); }
        return result.toString().trim();
    }

    /**
     * 수면 점수 계산 ///////////////////////////////////////////////////
     */
    private int generateSleepScore(Sleep sleep) {
        double score1 = calculateSleepScore1(sleep.getWorkdayBedtime(), sleep.getWorkdayWakeup()); // 근무일 수면 점수
        double score2 = calculateSleepScore2(sleep.getDayoffBedtime(), sleep.getDayoffWakeup()); // 휴무일 수면 점수
        double basicScore = (score1 + score2) / 2;
        return globalService.applySatisfaction(basicScore, sleep.getSatisfaction(), Label.SLEEP);
    }

    // 근무일 수면 점수
    private double calculateSleepScore1(double bedtime, double wakeupTime) {
        double sleepHours = calculateSleepHours(bedtime, wakeupTime);
        double sleepScore = calculateSleepHoursScore(sleepHours, 7.5, 8.5);
        double bedtimeScore = calculateBedtimeScore(bedtime, 22.0, 23.0);
        double wakeupScore = calculateWakeupScore(wakeupTime, 30.0, 31.0);
        return sleepScore * (0.6) + bedtimeScore * (0.2) + wakeupScore * (0.2);
    }

    // 휴무일 수면 점수
    private double calculateSleepScore2(double bedtime, double wakeupTime) {
        double sleepHours = calculateSleepHours(bedtime, wakeupTime);
        double sleepScore = calculateSleepHoursScore(sleepHours, 8.0, 9.5);
        double bedtimeScore = calculateBedtimeScore(bedtime, 23.0, 24.5);
        double wakeupScore = calculateWakeupScore(wakeupTime, 31.0, 33.5);
        return sleepScore * (0.6) + bedtimeScore * (0.2) + wakeupScore * (0.2);
    }

    // 수면 시간 계산
    private double calculateSleepHours(double bedtime, double wakeup) {
        return wakeup - bedtime;
    }

    // 수면 시간 점수
    private double calculateSleepHoursScore(double hours, double minHours, double maxHours) {
        if (hours <= minHours) {
            return (hours / minHours) * 100;
        } else if (hours <= maxHours) {
            return 100;
        } else {
            return 100 - (100 / (24 - maxHours)) * (hours - maxHours);
        }
    }

    // 취침 시각 점수
    private double calculateBedtimeScore(double bedtime, double minBedtime, double maxBedtime) {
        if (bedtime <= minBedtime) {
            return (100 / (minBedtime - 12) * (bedtime - 12));
        } else if (bedtime <= maxBedtime) {
            return 100;
        } else {
            return (100 / (maxBedtime - 48) * (bedtime - 48));
        }
    }

    // 기상 시각 점수
    private double calculateWakeupScore(double wakeup, double minWakeup, double maxWakeup) {
        if (wakeup <= minWakeup) {
            return (100 / (minWakeup - 12) * (wakeup - 12));
        } else if (wakeup <= maxWakeup) {
            return 100;
        } else {
            return (100 / (maxWakeup - 48) * (wakeup - 48));
        }
    }
}