package com.hexcode.pro_clock_out.wolibal.service;

import com.hexcode.pro_clock_out.member.domain.Member;
import com.hexcode.pro_clock_out.member.exception.WolibalNotFoundException;
import com.hexcode.pro_clock_out.member.service.MemberService;
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
    private final MemberService memberService;
    private final WolibalRepository wolibalRepository;
    private final WorkRepository workRepository;
    private final RestRepository restRepository;
    private final SleepRepository sleepRepository;
    private final PersonalRepository personalRepository;
    private final HealthRepository healthRepository;

    // 매일 정각에 워라밸 자동 생성
    public void createAutoWolibal() {
        List<Member> allMembers = memberService.findAllMembers();
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        allMembers.forEach(member -> {
            Optional<Wolibal> existingWolibalOpt = wolibalRepository.findByDateAndMember(yesterday, member);
            if (existingWolibalOpt.isPresent() && existingWolibalOpt.get().getScore() != 0) {
                Wolibal newWolibal = Wolibal.builder()
                        .date(today)
                        .member(member)
                        .build();
                wolibalRepository.save(newWolibal);
                Wolibal previousWolibal = existingWolibalOpt.get();

                Work previousWork = workRepository.findByWolibal(previousWolibal).orElse(null);
                if (previousWork != null) {
                    Work newWork = Work.builder()
                            .dayWorkingHours(previousWork.getDayWorkingHours())
                            .weekWorkingDays(previousWork.getWeekWorkingDays())
                            .workStress(previousWork.getWorkStress())
                            .satisfaction(previousWork.getSatisfaction())
                            .wolibal(newWolibal)
                            .build();
                    newWork.setScore(previousWork.getScore());
                    workRepository.save(newWork);
                }

                Rest previousRest = restRepository.findByWolibal(previousWolibal).orElse(null);
                if (previousRest != null) {
                    Rest newRest = Rest.builder()
                            .workdayRest(previousRest.getWorkdayRest())
                            .dayoffRest(previousRest.getDayoffRest())
                            .satisfaction(previousRest.getSatisfaction())
                            .wolibal(newWolibal)
                            .build();
                    newRest.setScore(previousRest.getScore());
                    restRepository.save(newRest);
                }

                Sleep previousSleep = sleepRepository.findByWolibal(previousWolibal).orElse(null);
                if (previousSleep != null) {
                    Sleep newSleep = Sleep.builder()
                            .workdayBedtime(previousSleep.getWorkdayBedtime())
                            .workdayWakeup(previousSleep.getWorkdayWakeup())
                            .dayoffBedtime(previousSleep.getDayoffBedtime())
                            .dayoffWakeup(previousSleep.getDayoffWakeup())
                            .satisfaction(previousSleep.getSatisfaction())
                            .wolibal(newWolibal)
                            .build();
                    newSleep.setScore(previousSleep.getScore());
                    sleepRepository.save(newSleep);
                }

                Personal previousPersonal = personalRepository.findByWolibal(previousWolibal).orElse(null);
                if (previousPersonal != null) {
                    Personal newPersonal = Personal.builder()
                            .togetherTime(previousPersonal.getTogetherTime())
                            .hobbyTime(previousPersonal.getHobbyTime())
                            .satisfaction(previousPersonal.getSatisfaction())
                            .wolibal(newWolibal)
                            .build();
                    newPersonal.setScore(previousPersonal.getScore());
                    personalRepository.save(newPersonal);
                }

                Health previousHealth = healthRepository.findByWolibal(previousWolibal).orElse(null);
                if (previousHealth != null) {
                    Health newHealth = Health.builder()
                            .cardioFrequency(previousHealth.getCardioFrequency())
                            .cardioTime(previousHealth.getCardioTime())
                            .strengthFrequency(previousHealth.getStrengthFrequency())
                            .strengthTime(previousHealth.getStrengthTime())
                            .dietQuality(previousHealth.getDietQuality())
                            .satisfaction(previousHealth.getSatisfaction())
                            .wolibal(newWolibal)
                            .build();
                    newHealth.setScore(previousHealth.getScore());
                    healthRepository.save(newHealth);

                    newWolibal.updateScore();
                    wolibalRepository.save(newWolibal);
                }
            }
        });
    }

    public Wolibal findTodayWolibalByMemberId(final Long memberId) {
        Member member = memberService.findMemberById(memberId);
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));LocalDate.now();
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
                .orElseThrow(WolibalNotFoundException::new);
    }

    public Work findWorkByWolibal(Wolibal wolibal) {
        return workRepository.findByWolibal(wolibal)
                .orElseThrow(WorkNotFoundException::new);
    }

    public Rest findRestByWolibal(Wolibal wolibal) {
        return restRepository.findByWolibal(wolibal)
                .orElseThrow(RestNotFoundException::new);
    }

    public Sleep findSleepByWolibal(Wolibal wolibal) {
        return sleepRepository.findByWolibal(wolibal)
                .orElseThrow(SleepNotFoundException::new);
    }

    public Personal findPersonalByWolibal(Wolibal wolibal) {
        return personalRepository.findByWolibal(wolibal)
                .orElseThrow(PersonalNotFoundException::new);
    }

    public Health findHealthByWolibal(Wolibal wolibal) {
        return healthRepository.findByWolibal(wolibal)
                .orElseThrow(HealthNotFoundException::new);
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
        wolibal.updateScore();
        wolibalRepository.save(wolibal);
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
        rest.setScore(generateRestScore(rest));
        restRepository.save(rest);
        wolibal.updateScore();
        wolibalRepository.save(wolibal);
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
        sleep.setScore(generateSleepScore(sleep));
        sleepRepository.save(sleep);
        wolibal.updateScore();
        wolibalRepository.save(wolibal);
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
        personal.setScore(generatePersonalScore(personal));
        personalRepository.save(personal);
        wolibal.updateScore();
        wolibalRepository.save(wolibal);
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
        health.setScore(generateHealthScore(health));
        healthRepository.save(health);
        wolibal.updateScore();
        wolibalRepository.save(wolibal);
        return CreateWolibalResponse.createWith(wolibal);
    }

    // 워라밸 항목별 만족도로 점수 업데이트
    public void updateWorkBySatisfaction(Wolibal wolibal, int satisfaction) {
        Work work = findWorkByWolibal(wolibal);
        work.setSatisfaction(satisfaction);
        work.setScore(applySatisfaction(work.getScore(), satisfaction));
    }

    public void updateRestBySatisfaction(Wolibal wolibal, int satisfaction) {
        Rest rest = findRestByWolibal(wolibal);
        rest.setSatisfaction(satisfaction);
        rest.setScore(applySatisfaction(rest.getScore(), satisfaction));
    }

    public void updateSleepBySatisfaction(Wolibal wolibal, int satisfaction) {
        Sleep sleep = findSleepByWolibal(wolibal);
        sleep.setSatisfaction(satisfaction);
        sleep.setScore(applySatisfaction(sleep.getScore(), satisfaction));
    }

    public void updatePersonalBySatisfaction(Wolibal wolibal, int satisfaction) {
        Personal personal = findPersonalByWolibal(wolibal);
        personal.setSatisfaction(satisfaction);
        personal.setScore(applySatisfaction(personal.getScore(), satisfaction));
    }

    public void updateHealthBySatisfaction(Wolibal wolibal, int satisfaction) {
        Health health = findHealthByWolibal(wolibal);
        health.setSatisfaction(satisfaction);
        health.setScore(applySatisfaction(health.getScore(), satisfaction));
    }

    // 워라밸 항목별 데이터로 점수 업데이트
    public UpdateWolibalResponse updateWork(Long workId, Long memberId, UpdateWorkRequest dto) {
        Wolibal wolibal = findTodayWolibalByMemberId(memberId);
        Optional<Work> existingWorkOpt = workRepository.findByWolibal(wolibal);

        if (existingWorkOpt.isPresent()) {
            Work existingWork = existingWorkOpt.get();

            existingWork.setDayWorkingHours(dto.getDayWorkingHours());
            existingWork.setWeekWorkingDays(dto.getWeekWorkingDays());
            existingWork.setWorkStress(dto.getWorkStress());
            existingWork.setSatisfaction(dto.getWorkSatisfaction());

            existingWork.setScore(generateWorkScore(existingWork));
            workRepository.save(existingWork);
            return UpdateWolibalResponse.createWith(wolibal);
        } else {
            throw new WorkNotFoundException(workId);
        }
    }

    public UpdateWolibalResponse updateRest(Long restId, Long memberId, UpdateRestRequest dto) {
        Wolibal wolibal = findTodayWolibalByMemberId(memberId);
        Optional<Rest> existingRestOpt = restRepository.findByWolibal(wolibal);

        if (existingRestOpt.isPresent()) {
            Rest existingRest = existingRestOpt.get();

            existingRest.setWorkdayRest(dto.getWorkdayRest());
            existingRest.setDayoffRest(dto.getDayoffRest());
            existingRest.setSatisfaction(dto.getRestSatisfaction());

            existingRest.setScore(generateRestScore(existingRest));
            restRepository.save(existingRest);
            return UpdateWolibalResponse.createWith(wolibal);
        } else {
            throw new RestNotFoundException(restId);
        }
    }

    public UpdateWolibalResponse updateSleep(Long sleepId, Long memberId, UpdateSleepRequest dto) {
        Wolibal wolibal = findTodayWolibalByMemberId(memberId);
        Optional<Sleep> existingSleepOpt = sleepRepository.findByWolibal(wolibal);

        if (existingSleepOpt.isPresent()) {
            Sleep existingSleep = existingSleepOpt.get();
            existingSleep.setWorkdayBedtime(dto.getWorkdayBedtime());
            existingSleep.setWorkdayWakeup(dto.getWorkdayWakeup());
            existingSleep.setDayoffBedtime(dto.getDayoffBedtime());
            existingSleep.setDayoffWakeup(dto.getDayoffWakeup());
            existingSleep.setSatisfaction(dto.getSleepSatisfaction());

            existingSleep.setScore(generateSleepScore(existingSleep));
            sleepRepository.save(existingSleep);
            return UpdateWolibalResponse.createWith(wolibal);
        } else {
            throw new SleepNotFoundException(sleepId);
        }
    }

    public UpdateWolibalResponse updatePersonal(Long personalId, Long memberId, UpdatePersonalRequest dto) {
        Wolibal wolibal = findTodayWolibalByMemberId(memberId);
        Optional<Personal> existingPersonalOpt = personalRepository.findByWolibal(wolibal);

        if (existingPersonalOpt.isPresent()) {
            Personal existingPersonal = existingPersonalOpt.get();
            existingPersonal.setTogetherTime(dto.getTogetherTime());
            existingPersonal.setHobbyTime(dto.getHobbyTime());
            existingPersonal.setSatisfaction(dto.getPersonalSatisfaction());

            existingPersonal.setScore(generatePersonalScore(existingPersonal));
            personalRepository.save(existingPersonal);
            return UpdateWolibalResponse.createWith(wolibal);
        } else {
            throw new PersonalNotFoundException(personalId);
        }
    }

    public UpdateWolibalResponse updateHealth(Long healthId, Long memberId, UpdateHealthRequest dto) {
        Wolibal wolibal = findTodayWolibalByMemberId(memberId);
        Optional<Health> existingHealthOpt = healthRepository.findByWolibal(wolibal);

        if (existingHealthOpt.isPresent()) {
            Health existingHealth = existingHealthOpt.get();

            existingHealth.setCardioFrequency(dto.getCardioFrequency());
            existingHealth.setCardioTime(dto.getCardioTime());
            existingHealth.setStrengthFrequency(dto.getStrengthFrequency());
            existingHealth.setStrengthTime(dto.getStrengthTime());
            existingHealth.setSatisfaction(dto.getHealthSatisfaction());

            existingHealth.setScore(generateHealthScore(existingHealth));
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

    public FindScoreRankAvgResponse findWork(Long memberId, Long workId) {
        Work work = workRepository.findById(workId)
                .orElseThrow(WorkNotFoundException::new);
        return createScoreRankAvgResponse(workId, work.getScore(), "work");
    }

    public FindScoreRankAvgResponse findRest(Long memberId, Long restId) {
        Rest rest = restRepository.findById(restId)
                .orElseThrow(RestNotFoundException::new);
        return createScoreRankAvgResponse(restId, rest.getScore(), "rest");
    }

    public FindScoreRankAvgResponse findSleep(Long memberId, Long sleepId) {
        Sleep sleep = sleepRepository.findById(sleepId)
                .orElseThrow(SleepNotFoundException::new);
        return createScoreRankAvgResponse(sleepId, sleep.getScore(), "sleep");
    }

    public FindScoreRankAvgResponse findPersonal(Long memberId, Long personalId) {
        Personal personal = personalRepository.findById(personalId)
                .orElseThrow(PersonalNotFoundException::new);
        return createScoreRankAvgResponse(personalId, personal.getScore(), "personal");
    }

    public FindScoreRankAvgResponse findHealth(Long memberId, Long healthId) {
        Health health = healthRepository.findById(healthId)
                .orElseThrow(HealthNotFoundException::new);
        return createScoreRankAvgResponse(healthId, health.getScore(), "health");
    }

    public FindWolibalTransitionsResponse findTransitions(Long memberId) {
        Member member = memberService.findMemberById(memberId);
        Pageable pageable = PageRequest.of(0, 10);
        List<Wolibal> totals10 = wolibalRepository.findRecent10(member, pageable);
        List<Work> works10 = workRepository.findRecent10(member, pageable);
        List<Rest> rests10 = restRepository.findRecent10(member, pageable);
        List<Sleep> sleeps10 = sleepRepository.findRecent10(member, pageable);
        List<Personal> personals10 = personalRepository.findRecent10(member, pageable);
        List<Health> healths10 = healthRepository.findRecent10(member, pageable);
        return FindWolibalTransitionsResponse.createWith(totals10, works10, rests10, sleeps10, personals10, healths10);
    }

//    public FindLabelsWolibalResponse findLabelsWolibal(Long memberId, String option) {
//        Wolibal wolibal = findTodayWolibalByMemberId(memberId);
//        Work work = findWorkByWolibal(wolibal);
//        Rest rest = findRestByWolibal(wolibal);
//        Sleep sleep = findSleepByWolibal(wolibal);
//        Personal personal = findPersonalByWolibal(wolibal);
//        Health health = findHealthByWolibal(wolibal);
//        FindScoreRankAvgResponse workDto = createScoreRankAvgResponse(work.getId(), work.getScore(), "work");
//        FindScoreRankAvgResponse restDto = createScoreRankAvgResponse(rest.getId(), rest.getScore(), "rest");
//        FindScoreRankAvgResponse sleepDto = createScoreRankAvgResponse(sleep.getId(), sleep.getScore(), "sleep");
//        FindScoreRankAvgResponse personalDto = createScoreRankAvgResponse(personal.getId(), personal.getScore(), "personal");
//        FindScoreRankAvgResponse healthDto = createScoreRankAvgResponse(health.getId(), health.getScore(), "health");
//        return FindLabelsWolibalResponse.createWith(memberId, workDto, restDto, sleepDto, personalDto, healthDto);
//    }

    private FindScoreRankAvgResponse createScoreRankAvgResponse(Long id, int score, String label) {
        long higherCount = calculateHigherCount(label, score);
//        int rank = calculateRank(higherCount);
        int rank = (int) higherCount;
        int avg = getAverage(label);
        return FindScoreRankAvgResponse.builder()
                .id(id)
                .score(score)
                .rank(rank)
                .avg(avg)
                .build();
    }

    private int calculateRank(long higherCount) {
        long allCount = wolibalRepository.count();
        return (int) ((higherCount * 100) / allCount);
    }

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
        if (hours <= 8) {
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
        double wakeupScore = calculateWakeupScore(wakeupTime, 31.0, 33.5);
        return sleepScore * (0.6) + bedtimeScore * (0.2) + wakeupScore * (0.2);
    }

    // 수면 시간 계산
    private static double calculateSleepHours(double bedtime, double wakeup) {
        return wakeup - bedtime;
    }

    // 수면 시간 점수
    private static double calculateSleepHoursScore(double hours, double minHours, double maxHours) {
        if (hours <= minHours) {
            return (hours / minHours) * 100;
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
        return Math.max(1, Math.min(100, score));
    }
}