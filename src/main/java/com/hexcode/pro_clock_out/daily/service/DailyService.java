package com.hexcode.pro_clock_out.daily.service;

import com.hexcode.pro_clock_out.daily.domain.Daily;
import com.hexcode.pro_clock_out.daily.domain.DailyGoal;
import com.hexcode.pro_clock_out.daily.domain.Goal;
import com.hexcode.pro_clock_out.daily.dto.*;
import com.hexcode.pro_clock_out.daily.exception.DailyNotFoundException;
import com.hexcode.pro_clock_out.daily.exception.GoalNotFoundException;
import com.hexcode.pro_clock_out.daily.repository.DailyRepository;
import com.hexcode.pro_clock_out.member.domain.Member;
import com.hexcode.pro_clock_out.member.exception.MemberNotFoundException;
import com.hexcode.pro_clock_out.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DailyService {
    private final DailyRepository dailyRepository;
    private final MemberService memberService;

    private LocalDate date;
    private List<DailyGoal> completedGoal;

    public Daily findDailyById(final Long dailyId) {
        return dailyRepository.findById(dailyId)
                .orElseThrow(()-> new DailyNotFoundException(dailyId));
    }

//    public DailyGoal findGoalById(final Long goalId) {
//        return goalRepository.findById(goalId)
//                .orElseThrow(()-> new GoalNotFoundException(goalId));
//    }

    public Daily findDailyByMember(final Member member) {
        return dailyRepository.findDailyByMember(member)
                .orElseThrow(()-> new MemberNotFoundException(member.getId()));
    }

    public Goal findGoalByMember(final Member member) {
        return dailyRepository.findGoalByMember(member)
                .orElseThrow(()-> new GoalNotFoundException(member.getId()));
    }

    // 발자국 상세 조회
    public FindDailyDetailResponse findDailyDetail(Long dailyId, Long memberId) {
        Daily daily = findDailyById(dailyId);
        return FindDailyDetailResponse.createWith(daily, date, completedGoal);
    }
    // 연간 발자국 조회
    public FindTotalDailyResponse findTotalDaily(Long memberId) {
        Member member = memberService.findMemberById(memberId);
        Daily daily = findDailyByMember(member);
        return FindTotalDailyResponse.createWith(daily, date);
    }

    // 발자국 추가
    public UpdateDailyResponse addDaily(Long dailyId, Long memberId, UpdateDailyRequest request) {
        Daily daily = findDailyById(dailyId);
        UpdateDailyData updateDailyData = UpdateDailyData.createWith(request);
        dailyRepository.save(daily);
        return UpdateDailyResponse.createWith(daily);
    }

    // 발자국 수정
    public UpdateDailyResponse updateDaily(Long dailyId, Long memberId, UpdateDailyRequest request) {
        Daily daily = findDailyById(dailyId);
        UpdateDailyData updateDailyData = UpdateDailyData.createWith(request);
        dailyRepository.save(daily);
        return UpdateDailyResponse.createWith(daily);
    }

    // 목표 활동 조회
    public FindGoalResponse findGoals(Long dailyId, Long memberId) {
        Member member = memberService.findMemberById(memberId);
        Goal goal = findGoalByMember(member);
        return FindGoalResponse.createWith(goal);

    }
    // 목표 활동 추가
//    public UpdateGoalResponse addGoals(Long dailyId, Long goalId, Long memberId, UpdateGoalRequest request) {
//        Goal goal = findGoalById(goalId);
//        UpdateGoalData updateGoalData = UpdateGoalData.createWith(request);
//        dailyRepository.save(goal);
//        return UpdateGoalResponse.createWith(goal);

//    }

    // 목표 활동 삭제
//    public DeleteGoalResponse deleteGoals(Long dailyId, Long goalId, Long memberId,) {
//        Goal goal = findGoalById(goalId);
//        dailyRepository.delete(goal);
//        return DeleteGoalResponse.createWith(goal);
//    }


}
