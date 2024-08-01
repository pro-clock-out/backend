package com.hexcode.pro_clock_out.wolibal.service;

import com.hexcode.pro_clock_out.member.domain.Member;
import com.hexcode.pro_clock_out.member.service.MemberService;
import com.hexcode.pro_clock_out.wolibal.domain.*;
import com.hexcode.pro_clock_out.wolibal.dto.*;
import com.hexcode.pro_clock_out.wolibal.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class WolibalService {
    private final MemberService memberService;
    private final WolibalRepository wolibalRepository;
    private final WorkRepository workRepository;
    private final RestRepository restRepository;
    private final SleepRepository sleepRepository;
    private final PersonalRepository personalRepository;
    private final HealthRepository healthRepository;

    public Wolibal findTodayWolibalByMemberId(final Long memberId) {
        Member member = memberService.findMemberById(memberId);
        LocalDate today = LocalDate.now();
        return wolibalRepository.findByDateAndMember(today, member)
                .orElseGet(() -> {
                    Wolibal newWolibal = Wolibal.builder()
                            .date(today)
                            .member(member)
                            .build();
                    return wolibalRepository.save(newWolibal);
                });
    }

    public CreateWolibalResponse createWork(Long memberId, CreateWorkRequest dto) {
        Wolibal wolibal = findTodayWolibalByMemberId(memberId);
        Optional<Work> existingWork = workRepository.findByWolibal(wolibal);
        Work work;
        if (existingWork.isPresent()) {
            work = existingWork.get();
            work.setDayWorkingHours(dto.getDayWorkingHours());
            work.setWeekWorkingDays(dto.getWeekWorkingDays());
            work.setWorkStress(dto.getWorkStress());
            work.setSatisfaction(dto.getWorkSatisfaction());
        } else {
            work = Work.builder()
                    .dayWorkingHours(dto.getDayWorkingHours())
                    .weekWorkingDays(dto.getWeekWorkingDays())
                    .workStress(dto.getWorkStress())
                    .satisfaction(dto.getWorkSatisfaction())
                    .wolibal(wolibal)
                    .build();
        }
        work.setScore(generateWorkScore(work));
        workRepository.save(work);
        return CreateWolibalResponse.createWith(wolibal);
    }

    public CreateWolibalResponse createRest(Long memberId, CreateRestRequest dto) {
        Wolibal wolibal = findTodayWolibalByMemberId(memberId);
        Optional<Rest> existingRest = restRepository.findByWolibal(wolibal);
        Rest rest;
        if (existingRest.isPresent()) {
            rest = existingRest.get();
            rest.setWorkdayRest(dto.getWorkdayRest());
            rest.setDayoffRest(dto.getDayoffRest());
            rest.setSatisfaction(dto.getRestSatisfaction());
        } else {
            rest = Rest.builder()
                    .workdayRest(dto.getWorkdayRest())
                    .dayoffRest(dto.getDayoffRest())
                    .satisfaction(dto.getRestSatisfaction())
                    .wolibal(wolibal)
                    .build();
        }
        rest.updateScore(generateRestScore(rest));
        restRepository.save(rest);
        return CreateWolibalResponse.createWith(wolibal);
    }

    public CreateWolibalResponse createSleep(Long memberId, CreateSleepRequest dto) {
        Wolibal wolibal = findTodayWolibalByMemberId(memberId);
        Optional<Sleep> existingSleep = sleepRepository.findByWolibal(wolibal);
        Sleep sleep;
        if (existingSleep.isPresent()) {
            sleep = existingSleep.get();
            sleep.setWorkdayBedtime(dto.getWorkdayBedtime());
            sleep.setWorkdayWakeup(dto.getWorkdayWakeup());
            sleep.setDayoffBedtime(dto.getDayoffBedtime());
            sleep.setDayoffWakeup(dto.getDayoffWakeup());
            sleep.setSatisfaction(dto.getSleepSatisfaction());
        } else {
            sleep = Sleep.builder()
                    .workdayBedtime(dto.getWorkdayBedtime())
                    .workdayWakeup(dto.getWorkdayWakeup())
                    .dayoffBedtime(dto.getDayoffBedtime())
                    .dayoffWakeup(dto.getDayoffWakeup())
                    .satisfaction(dto.getSleepSatisfaction())
                    .wolibal(wolibal)
                    .build();
        }
        sleep.updateScore(generateSleepScore(sleep));
        sleepRepository.save(sleep);
        return CreateWolibalResponse.createWith(wolibal);
    }

    public CreateWolibalResponse createPersonal(Long memberId, CreatePersonalRequest dto) {
        Wolibal wolibal = findTodayWolibalByMemberId(memberId);
        Optional<Personal> existingPersonal = personalRepository.findByWolibal(wolibal);
        Personal personal;
        if (existingPersonal.isPresent()) {
            personal = existingPersonal.get();
            personal.setTogetherTime(dto.getTogetherTime());
            personal.setHobbyTime(dto.getHobbyTime());
            personal.setSatisfaction(dto.getPersonalSatisfaction());
        } else {
            personal = Personal.builder()
                    .togetherTime(dto.getTogetherTime())
                    .hobbyTime(dto.getHobbyTime())
                    .satisfaction(dto.getPersonalSatisfaction())
                    .wolibal(wolibal)
                    .build();
        }
        personal.updateScore(generatePersonalScore(personal));
        personalRepository.save(personal);
        return CreateWolibalResponse.createWith(wolibal);
    }

    public CreateWolibalResponse createHealth(Long memberId, CreateHealthRequest dto) {
        Wolibal wolibal = findTodayWolibalByMemberId(memberId);
        Optional<Health> existingHealth = healthRepository.findByWolibal(wolibal);
        Health health;
        if (existingHealth.isPresent()) {
            health = existingHealth.get();
            health.setCardioFrequency(dto.getCardioFrequency());
            health.setCardioTime(dto.getCardioTime());
            health.setStrengthFrequency(dto.getStrengthFrequency());
            health.setStrengthTime(dto.getStrengthTime());
            health.setDietQuality(dto.getDietQuality());
            health.setSatisfaction(dto.getHealthSatisfaction());
        } else {
            health = Health.builder()
                    .cardioFrequency(dto.getCardioFrequency())
                    .cardioTime(dto.getCardioTime())
                    .strengthFrequency(dto.getStrengthFrequency())
                    .strengthTime(dto.getStrengthTime())
                    .dietQuality(dto.getDietQuality())
                    .satisfaction(dto.getHealthSatisfaction())
                    .wolibal(wolibal)
                    .build();
        }
        health.updateScore(generateHealthScore(health));
        healthRepository.save(health);
        return CreateWolibalResponse.createWith(wolibal);
    }


//
//    public FindTotalWolibalResponse findTotalWolibal(Long memberId, String option) {
//        Member member = memberService.findMemberById(memberId);
//        Wolibal wolibal = findWolibalByMember(member);
//        WolibalScoreRankAvgDto totalDto = createScoreRankAvgDto(wolibal.getTotalScore(), "total");
//        return FindTotalWolibalResponse.createWith(wolibal, totalDto);
//    }
//
//    public FindLabelsWolibalResponse findLabelsWolibal(Long memberId, String option) {
//        Member member = memberService.findMemberById(memberId);
//        Wolibal wolibal = findWolibalByMember(member);
//        WolibalScoreRankAvgDto workDto = createScoreRankAvgDto(wolibal.getWorkScore(), "work");
//        WolibalScoreRankAvgDto restDto = createScoreRankAvgDto(wolibal.getRestScore(), "rest");
//        WolibalScoreRankAvgDto sleepDto = createScoreRankAvgDto(wolibal.getSleepScore(), "sleep");
//        WolibalScoreRankAvgDto personalDto = createScoreRankAvgDto(wolibal.getPersonalScore(), "personal");
//        WolibalScoreRankAvgDto healthDto = createScoreRankAvgDto(wolibal.getHealthScore(), "health");
//        return FindLabelsWolibalResponse.createWith(member.getId(), workDto, restDto, sleepDto, personalDto, healthDto);
//    }
//
//    private WolibalScoreRankAvgDto createScoreRankAvgDto(int score, String label) {
//        long higherCount = calculateHigherCount(label, score);
//        int rank = calculateRank(higherCount);
//        int avg = getAverage(label);
//        return WolibalScoreRankAvgDto.builder()
//                .score(score)
//                .rank(rank)
//                .avg(avg)
//                .build();
//    }
//
//    private int calculateRank(long higherCount) {
//        long allCount = wolibalRepository.count();
//        return (int) ((higherCount * 100) / allCount);
//    }
//
//    private long calculateHigherCount(String label, int score) {
//        return switch (label) {
//            case "total" -> wolibalRepository.countByTotalHigherThan(score);
//            case "work" -> wolibalRepository.countByWorkHigherThan(score);
//            case "rest" -> wolibalRepository.countByRestHigherThan(score);
//            case "sleep" -> wolibalRepository.countBySleepHigherThan(score);
//            case "personal" -> wolibalRepository.countByPersonalHigherThan(score);
//            case "health" -> wolibalRepository.countByHealthHigherThan(score);
//            default -> throw new IllegalArgumentException("Invalid label name: " + label);
//        };
//    }
//
//    private int getAverage(String label) {
//        return switch (label) {
//            case "total" -> wolibalRepository.getAverageTotal();
//            case "work" -> wolibalRepository.getAverageWork();
//            case "rest" -> wolibalRepository.getAverageRest();
//            case "sleep" -> wolibalRepository.getAverageSleep();
//            case "personal" -> wolibalRepository.getAveragePersonal();
//            case "health" -> wolibalRepository.getAverageHealth();
//            default -> throw new IllegalArgumentException("Invalid label name: " + label);
//        };
//    }

    /**
     * 작업 점수 계산 ///////////////////////////////////////////////////
     */
    private static int generateWorkScore(Work work) {
        double score1 = calculateWorkScore1(work.getDayWorkingHours());
        double score2 = calculateWorkScore2(work.getWeekWorkingDays());
        double score3 = calculateWorkScore3(work.getWorkStress());
        double basicScore = score1 * (0.35) + score2 * (0.2) + score3 * (0.45);
        log.info("work basic score: {}", basicScore);
        int result = applySatisfaction(basicScore, work.getSatisfaction());
        log.info("work result score: {}", result);
        return result;
    }

    private static double calculateWorkScore1(double hours) {
        if (hours < 8) {
            return (hours / 8) * 100;
        } else if (hours <= 9) {
            return 100;
        } else {
            return 100 - ((hours - 9) / 15) * 100;
        }
    }

    private static double calculateWorkScore2(int days) {
        return (days < 1) ? 0 :
                (days <= 2) ? 60 :
                (days == 3) ? 80 :
                (days <= 5) ? 100 :
                (days == 6) ? 20 :
                0;
    }

    private static double calculateWorkScore3(int stress) {
        if (stress < 1 || stress > 9) {
            throw new IllegalArgumentException("stress 는 1 이상 9 이하의 정수여야 합니다.");
        }
        return (double) (stress - 1) / 8 * 100;
    }

    /**
     * 휴식 점수 계산 ///////////////////////////////////////////////////
     */
    private static int generateRestScore(Rest rest) {
        double score1 = calculateRestScore1(rest.getWorkdayRest());
        double score2 = calculateRestScore2(rest.getDayoffRest());
        double basicScore = score1 * (0.4) + score2 * (0.6);
        return applySatisfaction(basicScore, rest.getSatisfaction());
    }

    // 근무일 휴식 시간 점수
    private static double calculateRestScore1(double hours) {
        if (hours < 2) {
            return hours / 2 * 100;
        } else if (hours <= 3) {
            return 100;
        } else if (hours <= 6) {
            return 100 - (hours - 3) / 3 * 70;
        } else {
            return 30 - (hours - 6) / 18 * 30;
        }
    }

    // 휴무일 휴식 시간 점수
    private static double calculateRestScore2(double hours) {
        if (hours < 6) {
            return hours / 6 * 100;
        } else if (hours <= 8) {
            return 100;
        } else {
            return 100 - ((hours - 8) / 16) * 100;
        }
    }

    /**
     * 수면 점수 계산 ///////////////////////////////////////////////////
     */
    private static int generateSleepScore(Sleep sleep) {
        double score1 = calculateSleepScore1(sleep.getWorkdayBedtime(), sleep.getWorkdayWakeup());
        double score2 = calculateSleepScore2(sleep.getDayoffBedtime(), sleep.getDayoffWakeup());
        double basicScore = (score1 + score2) / 2;
        return applySatisfaction(basicScore, sleep.getSatisfaction());
    }

    // 근무일 수면 점수
    private static double calculateSleepScore1(double bedtime, double wakeupTime) {
        double sleepHours = calculateSleepHours(bedtime, wakeupTime);
        double sleepScore = calculateSleepHoursScore(sleepHours, 7.5, 8.5);
        double bedtimeScore = calculateBedtimeScore(bedtime, 22.0, 23.0);
        double wakeupScore = calculateWakeupScore(wakeupTime, 6.0, 7.0);
        return sleepScore * (0.6) + bedtimeScore * (0.2) + wakeupScore * (0.2);
    }

    // 휴무일 수면 점수
    private static double calculateSleepScore2(double bedtime, double wakeupTime) {
        double sleepHours = calculateSleepHours(bedtime, wakeupTime);
        double sleepScore = calculateSleepHoursScore(sleepHours, 8.0, 9.5);
        double bedtimeScore = calculateBedtimeScore(bedtime, 23.0, 24.5);
        double wakeupScore = calculateWakeupScore(wakeupTime, 7.0, 9.5);
        return sleepScore * (0.6) + bedtimeScore * (0.2) + wakeupScore * (0.2);
    }

    // 수면 시간 계산
    private static double calculateSleepHours(double bedtime, double wakeup) {
        return wakeup < bedtime ? (24 - bedtime) + wakeup : wakeup - bedtime;
    }

    // 수면 시간 점수
    private static double calculateSleepHoursScore(double hours, double minHours, double maxHours) {
        if (hours <= minHours) {
            return (100 / minHours) * hours;
        } else if (hours <= maxHours) {
            return 100;
        } else {
            return 100 - (100 / (24 - maxHours)) * (hours - maxHours);
        }
    }

    // 취침 시각 점수
    private static double calculateBedtimeScore(double bedtime, double minBedtime, double maxBedtime) {
        if (bedtime <= minBedtime) {
            return (100 / (minBedtime - 12) * (bedtime - 12));
        } else if (bedtime <= maxBedtime) {
            return 100;
        } else {
            return (100 / (maxBedtime - 48) * (bedtime - 48));
        }
    }

    // 기상 시각 점수
    private static double calculateWakeupScore(double wakeup, double minWakeup, double maxWakeup) {
        if (wakeup <= minWakeup) {
            return (100 / (minWakeup - 12) * (wakeup - 12));
        } else if (wakeup <= maxWakeup) {
            return 100;
        } else {
            return (100 / (maxWakeup - 48) * (wakeup - 48));
        }
    }

    /**
     * 개인 생활 점수 계산 ///////////////////////////////////////////////////
     */
    private static int generatePersonalScore(Personal personal) {
        double score1 = calculatePersonalScore1(personal.getTogetherTime());
        double score2 = calculatePersonalScore2(personal.getHobbyTime());
        double basicScore = (score1 + score2) / 2;
        return applySatisfaction(basicScore, personal.getSatisfaction());
    }

    // 함께하는 시간 점수
    private static double calculatePersonalScore1(int togetherTime) {
        if (togetherTime < 1 || togetherTime > 9) {
            throw new IllegalArgumentException("togetherTime 은 1 이상 9 이하의 정수여야 합니다.");
        }
        return (double) (togetherTime - 1) / 8 * 100;
    }

    // 취미 활동 시간 점수
    private static double calculatePersonalScore2(int hobbyTime) {
        if (hobbyTime < 1 || hobbyTime > 9) {
            throw new IllegalArgumentException("hobbyTime 은 1 이상 9 이하의 정수여야 합니다.");
        }
        return (double) (hobbyTime - 1) / 8 * 100;
    }

    /**
     * 건강 점수 계산 ///////////////////////////////////////////////////
     */
    private static int generateHealthScore(Health health) {
        double score1 = calculateHealthScore1(health.getCardioFrequency(), health.getCardioTime());
        double score2 = calculateHealthScore2(health.getStrengthFrequency(), health.getStrengthTime());
        double score3 = calculateHealthScore3(health.getDietQuality());
        double basicScore = (score1 + score2 + score3) / 3;
        return applySatisfaction(basicScore, health.getSatisfaction());
    }

    // 유산소 운동 점수
    private static double calculateHealthScore1(int cardioFrequency, double cardioTime) {
        double frequencyScore = calculateFrequencyScore(cardioFrequency, 3, 5, 80);
        double timeScore = calculateTimeScore(cardioTime, 0.5, 1, 50);
        return (frequencyScore + timeScore) / 2;
    }

    // 근력 운동 점수
    private static double calculateHealthScore2(int strengthFrequency, double strengthTime) {
        double frequencyScore = calculateFrequencyScore(strengthFrequency, 2, 4, 70);
        double timeScore = calculateTimeScore(strengthTime, 0.75, 1.25, 70);
        return (frequencyScore + timeScore) / 2;
    }

    // 운동 횟수 점수 계산
    private static double calculateFrequencyScore(double frequency, double minFrequency, double maxFrequency, double limit) {
        log.info("frequency: {}", frequency);
        if (frequency < minFrequency) {
            return 100 / minFrequency * frequency;
        } else if (frequency <= maxFrequency) {
            return 100;
        } else if (frequency <= 7) {
            return ((limit - 100) / (7 - maxFrequency) * frequency) + ((100 * (7 - maxFrequency) - maxFrequency * (limit - 100)) / (7 - maxFrequency));
        }
        return 0;
    }

    // 운동 시간 점수 계산
    private static double calculateTimeScore(double time, double minTime, double maxTime, double limit) {
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
    private static double calculateHealthScore3(int dietQuality) {
        if (dietQuality < 1 || dietQuality > 9) {
            log.info("dietQuality: {}", dietQuality);
            throw new IllegalArgumentException("dietQuality 는 1 이상 9 이하의 정수여야 합니다.");
        }
        return (double) (dietQuality - 1) / 8 * 100;
    }

    /**
     * 만족도 점수 적용 ///////////////////////////////////////////////////
     */
    private static int applySatisfaction(double basicScore, int satisfaction) {
        int score = (int) (basicScore * (1 + ((satisfaction - 5) / 200.0)));
        return Math.max(0, Math.min(100, score));
    }
}
