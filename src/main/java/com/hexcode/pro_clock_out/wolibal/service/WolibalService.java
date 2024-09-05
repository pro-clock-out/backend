package com.hexcode.pro_clock_out.wolibal.service;

import com.hexcode.pro_clock_out.global.domain.Label;
import com.hexcode.pro_clock_out.global.service.GlobalService;
import com.hexcode.pro_clock_out.member.domain.Member;
import com.hexcode.pro_clock_out.member.exception.WolibalNotFoundException;
import com.hexcode.pro_clock_out.wolibal.domain.*;
import com.hexcode.pro_clock_out.wolibal.dto.*;
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

import static com.hexcode.pro_clock_out.global.domain.Label.*;

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
        wolibalRepository.save(wolibal);
        workService.initializeWork(wolibal);
        restService.initializeRest(wolibal);
        sleepService.initializeSleep(wolibal);
        personalService.initializePersonal(wolibal);
        healthService.initializeHealth(wolibal);
        wolibal.updateScore();
        return wolibal;
    }

    // 매일 정각에 워라밸 자동 생성
    public void createDailyWolibal() {
        List<Member> allMembers = globalService.findAllMembers();
        LocalDate yesterday = LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1);

        allMembers.forEach(member -> {
            Wolibal yesterdayWolibal = findWolibalByDateAndMember(yesterday, member);

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

    public Wolibal findWolibalByDateAndMember(LocalDate date, Member member) {
        return wolibalRepository.findByDateAndMember(date, member)
                .orElseThrow(() -> new WolibalNotFoundException(date, member.getId()));
    }

    public Wolibal findTodayWolibalByMember(Member member) {
        return findWolibalByDateAndMember(LocalDate.now(ZoneId.of("Asia/Seoul")), member);
    }

    // 워라밸 항목별 데이터로 점수 업데이트
    public UpdateWolibalResponse updateWork(Long memberId, UpdateWorkRequest dto) {
        Member member = globalService.findMemberById(memberId);
        Wolibal wolibal = findTodayWolibalByMember(member);
        workService.updateWorkByData(wolibal.getWork(), dto);
        wolibal.updateScore();
        wolibalRepository.save(wolibal);
        return UpdateWolibalResponse.createWith(wolibal);
    }

    public UpdateWolibalResponse updateRest(Long memberId, UpdateRestRequest dto) {
        Member member = globalService.findMemberById(memberId);
        Wolibal wolibal = findTodayWolibalByMember(member);
        restService.updateRestByData(wolibal.getRest(), dto);
        wolibal.updateScore();
        wolibalRepository.save(wolibal);
        return UpdateWolibalResponse.createWith(wolibal);
    }

    public UpdateWolibalResponse updateSleep(Long memberId, UpdateSleepRequest dto) {
        Member member = globalService.findMemberById(memberId);
        Wolibal wolibal = findTodayWolibalByMember(member);
        sleepService.updateSleepByData(wolibal.getSleep(), dto);
        wolibal.updateScore();
        wolibalRepository.save(wolibal);
        return UpdateWolibalResponse.createWith(wolibal);
    }

    public UpdateWolibalResponse updatePersonal(Long memberId, UpdatePersonalRequest dto) {
        Member member = globalService.findMemberById(memberId);
        Wolibal wolibal = findTodayWolibalByMember(member);
        personalService.updatePersonalByData(wolibal.getPersonal(), dto);
        wolibal.updateScore();
        wolibalRepository.save(wolibal);
        return UpdateWolibalResponse.createWith(wolibal);
    }

    public UpdateWolibalResponse updateHealth(Long memberId, UpdateHealthRequest dto) {
        Member member = globalService.findMemberById(memberId);
        Wolibal wolibal = findTodayWolibalByMember(member);
        healthService.updateHealthByData(wolibal.getHealth(), dto);
        wolibal.updateScore();
        wolibalRepository.save(wolibal);
        return UpdateWolibalResponse.createWith(wolibal);
    }

    public FindScoreRankAvgResponse findTotalWolibal(Long memberId) {
        Member member = globalService.findMemberById(memberId);
        Wolibal wolibal = findTodayWolibalByMember(member);
        return createScoreRankAvgResponse(wolibal.getId(), wolibal.getScore(), TOTAL);
    }

    public FindAllWolibalResponse findAllWolibals(Long memberId) {
        Member member = globalService.findMemberById(memberId);
        Wolibal wolibal = findTodayWolibalByMember(member);
        Work work = workService.findWorkByWolibal(wolibal);
        Rest rest = restService.findRestByWolibal(wolibal);
        Sleep sleep = sleepService.findSleepByWolibal(wolibal);
        Personal personal = personalService.findPersonalByWolibal(wolibal);
        Health health = healthService.findHealthByWolibal(wolibal);
        FindScoreRankAvgResponse totalDto = createScoreRankAvgResponse(wolibal.getId(), wolibal.getScore(), TOTAL);
        FindScoreRankAvgResponse workDto = createScoreRankAvgResponse(work.getId(), work.getScore(), WORK);
        FindScoreRankAvgResponse restDto = createScoreRankAvgResponse(rest.getId(), rest.getScore(), REST);
        FindScoreRankAvgResponse sleepDto = createScoreRankAvgResponse(sleep.getId(), sleep.getScore(), SLEEP);
        FindScoreRankAvgResponse personalDto = createScoreRankAvgResponse(personal.getId(), personal.getScore(), PERSONAL);
        FindScoreRankAvgResponse healthDto = createScoreRankAvgResponse(health.getId(), health.getScore(), HEALTH);
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

    private FindScoreRankAvgResponse createScoreRankAvgResponse(Long id, int score, Label label) {
        int rank = (int) calculateHigherCount(label, score);
        int avg = getAverage(label);
        return FindScoreRankAvgResponse.builder()
                .id(id)
                .score(score)
                .rank(rank)
                .avg(avg)
                .build();
    }

    private long calculateHigherCount(Label label, int score) {
        return switch (label) {
            case TOTAL -> wolibalRepository.countByTotalHigherThan(score);
            case WORK -> workRepository.countByWorkHigherThan(score);
            case REST -> restRepository.countByRestHigherThan(score);
            case SLEEP -> sleepRepository.countBySleepHigherThan(score);
            case PERSONAL -> personalRepository.countByPersonalHigherThan(score);
            case HEALTH -> healthRepository.countByHealthHigherThan(score);
            default -> throw new IllegalArgumentException("Invalid label name: " + label);
        };
    }

    private int getAverage(Label label) {
        return switch (label) {
            case TOTAL -> wolibalRepository.getAverageTotal();
            case WORK -> workRepository.getAverageWork();
            case REST -> restRepository.getAverageRest();
            case SLEEP -> sleepRepository.getAverageSleep();
            case PERSONAL -> personalRepository.getAveragePersonal();
            case HEALTH -> healthRepository.getAverageHealth();
            default -> throw new IllegalArgumentException("Invalid label name: " + label);
        };
    }
}