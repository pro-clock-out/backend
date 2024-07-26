package com.hexcode.pro_clock_out.daily.service;

import com.hexcode.pro_clock_out.daily.domain.Daily;
import com.hexcode.pro_clock_out.daily.domain.DailyGoal;
import com.hexcode.pro_clock_out.daily.domain.Goal;
import com.hexcode.pro_clock_out.daily.dto.*;
import com.hexcode.pro_clock_out.daily.exception.DailyNotFoundException;
import com.hexcode.pro_clock_out.daily.exception.GoalNotFoundException;
import com.hexcode.pro_clock_out.daily.repository.DailyGoalRepository;
import com.hexcode.pro_clock_out.daily.repository.DailyRepository;
import com.hexcode.pro_clock_out.daily.repository.GoalRepository;
import com.hexcode.pro_clock_out.member.domain.Member;
import com.hexcode.pro_clock_out.member.exception.MemberNotFoundException;
import com.hexcode.pro_clock_out.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    public Daily findDailyByMember(final Member member) {
        return dailyRepository.findDailyByMember(member)
                .orElseThrow(()-> new MemberNotFoundException(member.getId()));
    }

    public Goal findGoalByMember(final Member member) {
        return goalRepository.findGoalByMember(member)
                .orElseThrow(()-> new GoalNotFoundException(member.getId()));
    }

    public List<Goal> findGoalsByDailyId(Long dailyId) {
        List<DailyGoal> dailyGoals = dailyGoalRepository.findByDailyId(dailyId);
        return dailyGoals.stream()
                .map(DailyGoal::getGoal)
                .collect(Collectors.toList());
    }

    // 발자국 상세 조회
    public FindDailyDetailResponse findDailyDetail(Long dailyId, Long memberId) {
        Daily daily = findDailyById(dailyId);
        List<Goal> completedGoal = findGoalsByDailyId(dailyId);
        return FindDailyDetailResponse.createWith(daily, completedGoal);
    }

    // 연간 발자국 조회
    public FindTotalDailyResponse findTotalDaily(Long memberId) {
        Member member = memberService.findMemberById(memberId);
        Daily daily = findDailyByMember(member);
        return FindTotalDailyResponse.createWith(daily);
    }

    // 발자국 추가
    public CreateDailyResponse addDaily(Long memberId, CreateDailyRequest request) {
        Member member = memberService.findMemberById(memberId);
        Daily daily = Daily.builder()
                .date(request.getDate())
                .satisfaction(request.getSatisfaction())
                .content(request.getContent())
                .imageUrl(request.getImageUrl())
                .member(member)
                .build();
        dailyRepository.save(daily);
        return CreateDailyResponse.createWith(daily);
    }

    // 발자국 수정
    public UpdateDailyResponse updateDaily(Long dailyId, Long memberId, UpdateDailyRequest request) {
        Member member = memberService.findMemberById(memberId);
        Daily daily = findDailyById(dailyId);
        UpdateDailyData updateDailyData = UpdateDailyData.createWith(request);
        daily.updateDaily(updateDailyData);
        dailyRepository.save(daily);
        List<DailyGoal> dailyGoals = dailyGoalRepository.findByDailyId(dailyId);
        dailyGoalRepository.deleteAll(dailyGoals);
        for (Goal newGoal : request.getCompletedGoal()) {
            Goal goal = goalRepository.findById(newGoal.getId())
                    .orElseThrow(() -> new GoalNotFoundException());
            DailyGoal dailyGoal = DailyGoal.builder()
                    .daily(daily)
                    .goal(goal)
                    .build();
            dailyGoalRepository.save(dailyGoal);
        }
        return UpdateDailyResponse.createWith(daily);
    }


    // 목표 활동 조회
    public FindGoalResponse findGoals(Long memberId) {
        Member member = memberService.findMemberById(memberId);
        Goal goal = findGoalByMember(member);
        return FindGoalResponse.createWith(goal);

    }
    // 목표 활동 추가
    public UpdateGoalResponse addGoals(Long memberId, UpdateGoalRequest request) {
        Member member = memberService.findMemberById(memberId);
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
