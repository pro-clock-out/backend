package com.hexcode.pro_clock_out.wolibal.service;

import com.hexcode.pro_clock_out.global.service.GlobalService;
import com.hexcode.pro_clock_out.member.domain.Member;
import com.hexcode.pro_clock_out.member.exception.WolibalNotFoundException;
import com.hexcode.pro_clock_out.wolibal.domain.*;
import com.hexcode.pro_clock_out.wolibal.dto.*;
import com.hexcode.pro_clock_out.wolibal.exception.*;
import com.hexcode.pro_clock_out.wolibal.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class WolibalService {
    private final GlobalService globalService;

    private final WolibalRepository wolibalRepository;
    private final WorkRepository workRepository;
    private final RestRepository restRepository;
    private final SleepRepository sleepRepository;
    private final PersonalRepository personalRepository;
    private final HealthRepository healthRepository;

    private final WorkService workService;
    private final RestService restService;
    private final SleepService sleepService;
    private final PersonalService personalService;
    private final HealthService healthService;

    // Wolibal 초기화
    public Wolibal initializeWolibal(Member member) {
        Wolibal wolibal = Wolibal.builder()
                .member(member)
                .date(LocalDate.now(ZoneId.of("Asia/Seoul")))
                .build();
        workService.initializeWork(wolibal);
        restService.initializeRest(wolibal);
        sleepService.initializeSleep(wolibal);
        personalService.initializePersonal(wolibal);
        healthService.initializeHealth(wolibal);
        wolibal.updateScore();
        wolibalRepository.save(wolibal);
        return wolibal;
    }

    // 매일 정각에 워라밸 자동 생성
    public void createDailyWolibal() {
        List<Member> allMembers = globalService.findAllMembers();
        LocalDate yesterday = LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1);

        allMembers.forEach(member -> {
            Wolibal yesterdayWolibal = findWolibalByDateAndMember(yesterday, member);
            if (yesterdayWolibal.getScore() == null) { return; }

            Wolibal todayWolibal = initializeWolibal(member);
            workService.createAutoWork(yesterdayWolibal, todayWolibal);
            restService.createAutoRest(yesterdayWolibal, todayWolibal);
            sleepService.createAutoSleep(yesterdayWolibal, todayWolibal);
            personalService.createAutoPersonal(yesterdayWolibal, todayWolibal);
            healthService.createAutoHealth(yesterdayWolibal, todayWolibal);
            todayWolibal.updateScore();
            wolibalRepository.save(todayWolibal);
        });
    }

    public Wolibal findTodayWolibalByMemberId(final Long memberId) {
        Member member = globalService.findMemberById(memberId);
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        return wolibalRepository.findByDateAndMember(today, member)
                .orElseGet(() -> {
                    Wolibal newWolibal = Wolibal.builder()
                            .date(today)
                            .member(member)
                            .build();
                    return wolibalRepository.save(newWolibal);
                });
    }

    public Wolibal findWolibalByDateAndMember(LocalDate date, Member member) {
        return wolibalRepository.findByDateAndMember(date, member)
                .orElseThrow(() -> new WolibalNotFoundException(date, member.getId()));
    }

    // 워라밸 항목별 데이터로 점수 업데이트
    public UpdateWolibalResponse updateWork(Long workId, Long memberId, UpdateWorkRequest dto) {
        Member member = globalService.findMemberById(memberId);
        Wolibal wolibal = workService.updateWorkByData(workId, dto);
        wolibal.updateScore();
        wolibalRepository.save(wolibal);
        return UpdateWolibalResponse.createWith(wolibal);
    }

    public UpdateWolibalResponse updateRest(Long restId, UpdateRestRequest dto) {
        Wolibal wolibal = restService.updateRestByData(restId, dto);
        wolibal.updateScore();
        wolibalRepository.save(wolibal);

        return UpdateWolibalResponse.createWith(wolibal);
    }

    public UpdateWolibalResponse updateSleep(Long sleepId, Long memberId, UpdateSleepRequest dto) {
        Wolibal wolibal = sleepService.updateSleepByData(sleepId, dto);
        wolibal.updateScore();
        wolibalRepository.save(wolibal);

        return UpdateWolibalResponse.createWith(wolibal);
    }

    public UpdateWolibalResponse updatePersonal(Long personalId, Long memberId, UpdatePersonalRequest dto) {
        Wolibal wolibal = findTodayWolibalByMemberId(memberId);
        Member member = globalService.findMemberById(memberId);
        Optional<Personal> existingPersonalOpt = personalRepository.findByWolibal(wolibal);

        if (existingPersonalOpt.isPresent()) {
            Personal existingPersonal = existingPersonalOpt.get();
            existingPersonal.setTogetherTime(dto.getTogetherTime());
            existingPersonal.setHobbyTime(dto.getHobbyTime());
            existingPersonal.setSatisfaction(dto.getPersonalSatisfaction());

            existingPersonal.setScore(generatePersonalScore(existingPersonal, member));
            personalRepository.save(existingPersonal);
            return UpdateWolibalResponse.createWith(wolibal);
        } else {
            throw new PersonalNotFoundException(personalId);
        }
    }

    public UpdateWolibalResponse updateHealth(Long healthId, Long memberId, UpdateHealthRequest dto) {
        Wolibal wolibal = findTodayWolibalByMemberId(memberId);
        Member member = globalService.findMemberById(memberId);
        Optional<Health> existingHealthOpt = healthRepository.findByWolibal(wolibal);

        if (existingHealthOpt.isPresent()) {
            Health existingHealth = existingHealthOpt.get();

            existingHealth.setCardioFrequency(dto.getCardioFrequency());
            existingHealth.setCardioTime(dto.getCardioTime());
            existingHealth.setStrengthFrequency(dto.getStrengthFrequency());
            existingHealth.setStrengthTime(dto.getStrengthTime());
            existingHealth.setSatisfaction(dto.getHealthSatisfaction());

            existingHealth.setScore(generateHealthScore(existingHealth, member));
            healthRepository.save(existingHealth);
            return UpdateWolibalResponse.createWith(wolibal);
        } else {
            throw new HealthNotFoundException(healthId);
        }
    }

    public FindScoreRankAvgResponse findTotalWolibal(Long memberId) {
        Wolibal wolibal = findTodayWolibalByMemberId(memberId);
        return createScoreRankAvgResponse(wolibal.getId(), wolibal.getScore(), "total");
    }

    public FindAllWolibalResponse findAllWolibals(Long memberId) {
        Wolibal wolibal = findTodayWolibalByMemberId(memberId);
        Work work = workService.findWorkByWolibal(wolibal);
        Rest rest = restService.findRestByWolibal(wolibal);
        Sleep sleep = sleepService.findSleepByWolibal(wolibal);
        Personal personal = personalService.findPersonalByWolibal(wolibal);
        Health health = healthService.findHealthByWolibal(wolibal);
        FindScoreRankAvgResponse totalDto = createScoreRankAvgResponse(wolibal.getId(), wolibal.getScore(), "total");
        FindScoreRankAvgResponse workDto = createScoreRankAvgResponse(work.getId(), work.getScore(), "work");
        FindScoreRankAvgResponse restDto = createScoreRankAvgResponse(rest.getId(), rest.getScore(), "rest");
        FindScoreRankAvgResponse sleepDto = createScoreRankAvgResponse(sleep.getId(), sleep.getScore(), "sleep");
        FindScoreRankAvgResponse personalDto = createScoreRankAvgResponse(personal.getId(), personal.getScore(), "personal");
        FindScoreRankAvgResponse healthDto = createScoreRankAvgResponse(health.getId(), health.getScore(), "health");
        return FindAllWolibalResponse.builder()
                .total(totalDto)
                .work(workDto)
                .rest(restDto)
                .sleep(sleepDto)
                .personal(personalDto)
                .health(healthDto)
                .build();
    }

    public FindWolibalTransitionsResponse findTransitions(Long memberId) {
        Member member = globalService.findMemberById(memberId);
        Pageable pageable = PageRequest.of(0, 10);
        List<Wolibal> totals10 = wolibalRepository.findRecent10(member, pageable);
        List<Work> works10 = workRepository.findRecent10(member, pageable);
        List<Rest> rests10 = restRepository.findRecent10(member, pageable);
        List<Sleep> sleeps10 = sleepRepository.findRecent10(member, pageable);
        List<Personal> personals10 = personalRepository.findRecent10(member, pageable);
        List<Health> healths10 = healthRepository.findRecent10(member, pageable);
        return FindWolibalTransitionsResponse.createWith(totals10, works10, rests10, sleeps10, personals10, healths10);
    }

    private FindScoreRankAvgResponse createScoreRankAvgResponse(Long id, int score, String label) {
//        long higherCount = calculateHigherCount(label, score);
//        int rank = calculateRank(higherCount);
        int rank = (int) calculateHigherCount(label, score);
        int avg = getAverage(label);
        return FindScoreRankAvgResponse.builder()
                .id(id)
                .score(score)
                .rank(rank)
                .avg(avg)
                .build();
    }

//    private int calculateRank(long higherCount) {
//        long allCount = wolibalRepository.count();
//        return (int) ((higherCount * 100) / allCount);
//    }

    private long calculateHigherCount(String label, int score) {
        return switch (label) {
            case "total" -> wolibalRepository.countByTotalHigherThan(score);
            case "work" -> workRepository.countByWorkHigherThan(score);
            case "rest" -> restRepository.countByRestHigherThan(score);
            case "sleep" -> sleepRepository.countBySleepHigherThan(score);
            case "personal" -> personalRepository.countByPersonalHigherThan(score);
            case "health" -> healthRepository.countByHealthHigherThan(score);
            default -> throw new IllegalArgumentException("Invalid label name: " + label);
        };
    }

    private int getAverage(String label) {
        return switch (label) {
            case "total" -> wolibalRepository.getAverageTotal();
            case "work" -> workRepository.getAverageWork();
            case "rest" -> restRepository.getAverageRest();
            case "sleep" -> sleepRepository.getAverageSleep();
            case "personal" -> personalRepository.getAveragePersonal();
            case "health" -> healthRepository.getAverageHealth();
            default -> throw new IllegalArgumentException("Invalid label name: " + label);
        };
    }

    /**
     * 개인 생활 점수 계산 ///////////////////////////////////////////////////
     */
    private static int generatePersonalScore(Personal personal, Member member) {
        double score1 = calculatePersonalScore1(personal.getTogetherTime()); // 함께하는 시간 점수
        double score2 = calculatePersonalScore2(personal.getHobbyTime()); // 취미 활동 시간 점수
        double basicScore = (score1 + score2) / 2;
        return applySatisfaction(basicScore, personal.getSatisfaction(), "개인 생활", member);
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
    private static int generateHealthScore(Health health, Member member) {
        double score1 = calculateHealthScore1(health.getCardioFrequency(), health.getCardioTime()); // 유산소 운동 점수
        double score2 = calculateHealthScore2(health.getStrengthFrequency(), health.getStrengthTime()); // 근력 운동 점수
        double score3 = calculateHealthScore3(health.getDietQuality());
        double basicScore = (score1 + score2 + score3) / 3;
        return applySatisfaction(basicScore, health.getSatisfaction(), "건강", member);
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
}