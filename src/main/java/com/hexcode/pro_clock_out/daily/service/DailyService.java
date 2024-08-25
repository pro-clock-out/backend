package com.hexcode.pro_clock_out.daily.service;

import com.hexcode.pro_clock_out.daily.domain.Daily;
import com.hexcode.pro_clock_out.daily.domain.DailyGoal;
import com.hexcode.pro_clock_out.daily.domain.Goal;
import com.hexcode.pro_clock_out.daily.dto.*;
import com.hexcode.pro_clock_out.daily.exception.DailyNotFoundException;
import com.hexcode.pro_clock_out.daily.exception.GoalAlreadyExistsException;
import com.hexcode.pro_clock_out.daily.exception.GoalLimitExceededException;
import com.hexcode.pro_clock_out.daily.exception.GoalNotFoundException;
import com.hexcode.pro_clock_out.daily.repository.DailyGoalRepository;
import com.hexcode.pro_clock_out.daily.repository.DailyRepository;
import com.hexcode.pro_clock_out.daily.repository.GoalRepository;
import com.hexcode.pro_clock_out.global.service.GlobalService;
import com.hexcode.pro_clock_out.member.domain.Member;
import com.hexcode.pro_clock_out.member.exception.MemberNotFoundException;
import com.hexcode.pro_clock_out.member.service.MemberService;
import com.hexcode.pro_clock_out.wolibal.domain.Wolibal;
import com.hexcode.pro_clock_out.wolibal.repository.WolibalRepository;
import com.hexcode.pro_clock_out.wolibal.service.WolibalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DailyService {
    private final WolibalRepository wolibalRepository;
    private final DailyRepository dailyRepository;
    private final GoalRepository goalRepository;
    private final DailyGoalRepository dailyGoalRepository;

    private final GlobalService globalService;
    private final WolibalService wolibalService;


    public Daily findDailyById(final Long dailyId) {
        return dailyRepository.findById(dailyId)
                .orElseThrow(()-> new DailyNotFoundException(dailyId));
    }

   public Goal findGoalById(final Long goalId) {
        return goalRepository.findById(goalId)
               .orElseThrow(()-> new GoalNotFoundException(goalId));
    }

    public List<Daily> findDailyByMember(final Member member) {
        List<Daily> totalDaily = dailyRepository.findDailyByMember(member);
        if (totalDaily.isEmpty()) {
            throw new MemberNotFoundException(member.getId());
        }
        return totalDaily;
    }

    public List<Goal> findGoalsByMember(final Member member) {
        List<Goal> totalGoal = goalRepository.findGoalsByMember(member);
        if (totalGoal.isEmpty()) {
            throw new GoalNotFoundException(member.getId());
        }
        return totalGoal;
    }

    public List<String> findGoalNamesByDailyId(Long dailyId) {
        List<DailyGoal> dailyGoals = dailyGoalRepository.findByDailyId(dailyId);
        return dailyGoals.stream()
                .map(dailyGoal -> dailyGoal.getGoal().getContent())
                .collect(Collectors.toList());
    }

    // 연간 발자국 조회
    public FindTotalDailyResponse findTotalDaily(Long memberId, int year) {
        Member member = globalService.findMemberById(memberId);
        List<Daily> dailyList = dailyRepository.findDailyByMember(member).stream()
                .filter(daily -> daily.getDate().getYear() == year)
                .toList();

        List<DailySimpleData> filteredDailyYear = dailyList.stream()
                .map(daily -> {
                    Wolibal wolibal = wolibalService.findWolibalByDateAndMember(daily.getDate(), daily.getMember());
                    int totalScore = wolibal.getScore();
                    return DailySimpleData.createWith(daily, totalScore);
                })
                .toList();

        return FindTotalDailyResponse.createWith(filteredDailyYear);
    }

    // 발자국 상세 조회
    public FindDailyDetailResponse findDailyDetail(Long dailyId, Long memberId) {
        Daily daily = findDailyById(dailyId);
        List<String> completedGoals = findGoalNamesByDailyId(dailyId);
        return FindDailyDetailResponse.createWith(daily, completedGoals);
    }

    // 발자국 추가
    public CreateDailyResponse addDaily(Long memberId, CreateDailyRequest request) {
        Member member = globalService.findMemberById(memberId);

        Daily daily = Daily.builder()
                .date(request.getDate())
                .content(request.getContent())
                .workSatisfaction(request.getWorkSatisfaction())
                .restSatisfaction(request.getRestSatisfaction())
                .sleepSatisfaction(request.getSleepSatisfaction())
                .personalSatisfaction(request.getPersonalSatisfaction())
                .healthSatisfaction(request.getHealthSatisfaction())
                .imageUrl(request.getImageUrl())
                .member(member)
                .build();
        dailyRepository.save(daily);

        if (request.getCompletedGoals() != null) {
            for (String goalName : request.getCompletedGoals()) {
                Goal goal = goalRepository.findByContent(goalName)
                        .orElseThrow(GoalNotFoundException::new);
                DailyGoal dailyGoal = DailyGoal.builder()
                        .daily(daily)
                        .goal(goal)
                        .build();
                dailyGoalRepository.save(dailyGoal);
            }
        }

        // 데일리 항목별 만족도에 따라 워라밸 점수 업데이트
        Optional<Wolibal> existWolibal = wolibalRepository.findByDateAndMember(request.getDate(), member);
        if (existWolibal.isPresent()) {
            Wolibal wolibal = existWolibal.get();
            work.updateWorkBySatisfaction(wolibal, request.getWorkSatisfaction(), member);
            wolibalService.updateRestBySatisfaction(wolibal, request.getRestSatisfaction(), member);
            wolibalService.updateSleepBySatisfaction(wolibal, request.getSleepSatisfaction(), member);
            wolibalService.updatePersonalBySatisfaction(wolibal, request.getPersonalSatisfaction(), member);
            wolibalService.updateHealthBySatisfaction(wolibal, request.getHealthSatisfaction(), member);
        }
        return CreateDailyResponse.createWith(daily);
    }

    // 발자국 수정
    public UpdateDailyResponse updateDaily(Long dailyId, Long memberId, UpdateDailyRequest request) {
        Member member = globalService.findMemberById(memberId);

        Daily daily = findDailyById(dailyId);
        UpdateDailyData updateDailyData = UpdateDailyData.createWith(request);
        daily.updateDaily(updateDailyData);
        dailyRepository.save(daily);

        if (request.getCompletedGoals() != null) {
            List<DailyGoal> dailyGoals = dailyGoalRepository.findByDailyId(dailyId);
            dailyGoalRepository.deleteAll(dailyGoals);
            for (String newGoalName : request.getCompletedGoals()) {
                Goal goal = goalRepository.findByContent(newGoalName)
                        .orElseThrow(GoalNotFoundException::new);
                DailyGoal dailyGoal = DailyGoal.builder()
                        .daily(daily)
                        .goal(goal)
                        .build();
                dailyGoalRepository.save(dailyGoal);
            }
        }

        // 데일리 항목별 만족도에 따라 워라밸 점수 업데이트
        Optional<Wolibal> existWolibal = wolibalRepository.findByDateAndMember(request.getDate(), member);
        if (existWolibal.isPresent()) {
            Wolibal wolibal = existWolibal.get();
            wolibalService.updateWorkBySatisfaction(wolibal, request.getWorkSatisfaction(), member);
            wolibalService.updateRestBySatisfaction(wolibal, request.getRestSatisfaction(), member);
            wolibalService.updateSleepBySatisfaction(wolibal, request.getSleepSatisfaction(), member);
            wolibalService.updatePersonalBySatisfaction(wolibal, request.getPersonalSatisfaction(), member);
            wolibalService.updateHealthBySatisfaction(wolibal, request.getHealthSatisfaction(), member);
        }

        return UpdateDailyResponse.createWith(daily);
    }


    // 목표 활동 조회
    public FindGoalResponse findGoals(Long memberId) {
        Member member = globalService.findMemberById(memberId);
        List<Goal> goals = goalRepository.findGoalsByMember(member);
        return FindGoalResponse.createWith(goals);
    }

    // 목표 활동 추가
    public CreateGoalResponse addGoals(Long memberId, CreateGoalRequest request) {
        Member member = globalService.findMemberById(memberId);
        List<Goal> existingGoals = goalRepository.findGoalsByMember(member);
        if (existingGoals.size() >= 10) {
            throw new GoalLimitExceededException();
        }

        Optional<Goal> existingGoal = goalRepository.findByContent((request.getContent()));
        if (existingGoal.isPresent()) {
            throw new GoalAlreadyExistsException(request.getContent());
        }

        Goal goal = Goal.builder()
                .content(request.getContent())
                .category(request.getCategory())
                .member(member)
                .build();
        goalRepository.save(goal);
        return CreateGoalResponse.createWith(goal);
    }

    // 목표 활동 수정
    public UpdateGoalResponse updateGoals(Long goalId, Long memberId, UpdateGoalRequest request) {
        Member member = globalService.findMemberById(memberId);
        Goal goal = findGoalById(goalId);
        UpdateGoalData updateGoalData = UpdateGoalData.createWith(request);
        goal.updateGoals(updateGoalData);
        goalRepository.save(goal);
        return UpdateGoalResponse.createWith(goal);

    }

    // 목표 활동 삭제
    public DeleteGoalResponse deleteGoals(Long goalId, Long memberId) {
        Member member = globalService.findMemberById(memberId);
        Goal goal = findGoalById(goalId);
        goalRepository.delete(goal);
        return DeleteGoalResponse.createWith(goal);
    }

}
