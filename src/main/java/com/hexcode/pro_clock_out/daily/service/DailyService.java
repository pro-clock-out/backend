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
import com.hexcode.pro_clock_out.member.domain.Member;
import com.hexcode.pro_clock_out.member.exception.MemberNotFoundException;
import com.hexcode.pro_clock_out.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DailyService {
    private final DailyRepository dailyRepository;
    private final GoalRepository goalRepository;
    private final DailyGoalRepository dailyGoalRepository;
    private final MemberService memberService;


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
                .map(dailyGoal -> dailyGoal.getGoal().getName())
                .collect(Collectors.toList());
    }


    // 연간 발자국 조회
    public FindTotalDailyResponse findTotalDaily(Long memberId, int year) {
        Member member = memberService.findMemberById(memberId);
        List<Daily> dailyList = dailyRepository.findDailyByMember(member);
        List<Daily> filteredDailyYear = dailyList.stream()
                .filter(daily -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(daily.getDate());
                    int dailyYear = calendar.get(Calendar.YEAR);
                    return  dailyYear == year;
                })
                .collect(Collectors.toList());
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
        Member member = memberService.findMemberById(memberId);

        Daily daily = Daily.builder()
                .date(request.getDate())
                .workSatisfaction(request.getWorkSatisfaction())
                .restSatisfaction(request.getRestSatisfaction())
                .sleepSatisfaction(request.getSleepSatisfaction())
                .personalSatisfaction(request.getPersonalSatisfaction())
                .healthSatisfaction(request.getHealthSatisfaction())
                .content(request.getContent())
                .imageUrl(request.getImageUrl())
                .member(member)
                .build();
        dailyRepository.save(daily);

        if (request.getCompletedGoals() != null) {
            for (String goalName : request.getCompletedGoals()) {
                Goal goal = goalRepository.findByName(goalName)
                        .orElseThrow(GoalNotFoundException::new);
                DailyGoal dailyGoal = DailyGoal.builder()
                        .daily(daily)
                        .goal(goal)
                        .build();
                dailyGoalRepository.save(dailyGoal);
            }
        }

        return CreateDailyResponse.createWith(daily);
    }

    // 발자국 수정
    public UpdateDailyResponse updateDaily(Long dailyId, Long memberId, UpdateDailyRequest request) {
        Member member = memberService.findMemberById(memberId);

        Daily daily = findDailyById(dailyId);
        UpdateDailyData updateDailyData = UpdateDailyData.createWith(request);
        daily.updateDaily(updateDailyData);
        dailyRepository.save(daily);

        if (request.getCompletedGoals() != null) {
            List<DailyGoal> dailyGoals = dailyGoalRepository.findByDailyId(dailyId);
            dailyGoalRepository.deleteAll(dailyGoals);
            for (String newGoalName : request.getCompletedGoals()) {
                Goal goal = goalRepository.findByName(newGoalName)
                        .orElseThrow(GoalNotFoundException::new);
                DailyGoal dailyGoal = DailyGoal.builder()
                        .daily(daily)
                        .goal(goal)
                        .build();
                dailyGoalRepository.save(dailyGoal);
            }
        }
        return UpdateDailyResponse.createWith(daily);
    }


    // 목표 활동 조회
    public FindGoalResponse findGoals(Long memberId) {
        Member member = memberService.findMemberById(memberId);
        List<Goal> goals = goalRepository.findGoalsByMember(member);
        return FindGoalResponse.createWith(goals);
    }

    // 목표 활동 추가
    public UpdateGoalResponse addGoals(Long memberId, UpdateGoalRequest request) {
        Member member = memberService.findMemberById(memberId);
        List<Goal> existingGoals = goalRepository.findGoalsByMember(member);
        if (existingGoals.size() >= 10) {
            throw new GoalLimitExceededException();
        }

        Optional<Goal> existingGoal = goalRepository.findByName((request.getName()));
        if (existingGoal.isPresent()) {
            throw new GoalAlreadyExistsException(request.getName());
        }

        Goal goal = Goal.builder()
                .name(request.getName())
                .color(request.getColor())
                .member(member)
                .build();
        goalRepository.save(goal);
        return UpdateGoalResponse.createWith(goal);
    }


    // 목표 활동 삭제
    public DeleteGoalResponse deleteGoals(Long goalId, Long memberId) {
        Member member = memberService.findMemberById(memberId);
        Goal goal = findGoalById(goalId);
        goalRepository.delete(goal);
        return DeleteGoalResponse.createWith(goal);
    }

}
