package com.hexcode.pro_clock_out.wolibal.service;

import com.hexcode.pro_clock_out.global.domain.Label;
import com.hexcode.pro_clock_out.global.service.GlobalService;
import com.hexcode.pro_clock_out.wolibal.domain.Wolibal;
import com.hexcode.pro_clock_out.wolibal.domain.Work;
import com.hexcode.pro_clock_out.wolibal.dto.UpdateWorkRequest;
import com.hexcode.pro_clock_out.wolibal.exception.WorkNotFoundException;
import com.hexcode.pro_clock_out.wolibal.repository.WorkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class WorkService {
    private final GlobalService globalService;
    private final WorkRepository workRepository;

    public void initializeWork(Wolibal wolibal) {
        Work work = Work.builder()
                .wolibal(wolibal)
                .build();
        workRepository.save(work);
        wolibal.setWork(work);
    }

    public Work findWorkById(Long workId) {
        return workRepository.findById(workId)
                .orElseThrow(() -> new WorkNotFoundException(workId));
    }

    public Work findWorkByWolibal(Wolibal wolibal) {
        return workRepository.findByWolibal(wolibal)
                .orElseThrow(() -> new WorkNotFoundException(wolibal));
    }

    public void createAutoWork(Wolibal yesterdayWolibal, Wolibal todayWolibal) {
        Work yesterdayWork = findWorkByWolibal(yesterdayWolibal);
        Work todayWork = Work.builder()
                .score(yesterdayWork.getScore())
                .dayWorkingHours(yesterdayWork.getDayWorkingHours())
                .weekWorkingDays(yesterdayWork.getWeekWorkingDays())
                .workStress(yesterdayWork.getWorkStress())
                .satisfaction(yesterdayWork.getSatisfaction())
                .wolibal(todayWolibal)
                .build();
        workRepository.save(todayWork);
    }

    public void updateWorkBySatisfaction(Wolibal wolibal, int satisfaction) {
        Work work = findWorkByWolibal(wolibal);
        work.setSatisfaction(satisfaction);
        work.setScore(globalService.applySatisfaction(work.getScore(), satisfaction, Label.WORK));
    }

    public void updateWorkByData(Work work, UpdateWorkRequest dto) {
        work.setDayWorkingHours(dto.getDayWorkingHours());
        work.setWeekWorkingDays(dto.getWeekWorkingDays());
        work.setWorkStress(dto.getWorkStress());
        work.setSatisfaction(dto.getWorkSatisfaction());

        work.setScore(generateWorkScore(work));
        workRepository.save(work);
    }

    public String describeWorkData(Wolibal wolibal) {
        Work work = findWorkByWolibal(wolibal);
        Double dayWorkingHours = work.getDayWorkingHours();
        Integer weekWorkingDays = work.getWeekWorkingDays();
        Integer workStress = work.getWorkStress();
        Integer satisfaction = work.getSatisfaction();

        StringBuilder result = new StringBuilder();
        if (dayWorkingHours != null) { result.append(String.format("사용자는 하루에 %.1f시간 일한다.\n", dayWorkingHours)); }
        if (weekWorkingDays != null) { result.append(String.format("사용자는 일주일에 %d일 동안 일한다.\n", weekWorkingDays)); }
        if (workStress != null) { result.append(String.format("사용자의 업무 스트레스 점수는 %d점이다. 1일수록 스트레스가 많고, 9일수록 스트레스가 적다.\n", workStress)); }
        if (satisfaction != null) { result.append(String.format("사용자의 업무에 대한 워라밸 만족도는 %d점이다. 1일수록 매우 불만족이고, 9일수록 매우 만족이다.\n", satisfaction)); }
        return result.toString();
    }

    /**
     * 작업 점수 계산 ///////////////////////////////////////////////////
     */
    private int generateWorkScore(Work work) {
        double score1 = calculateWorkScore1(work.getDayWorkingHours()); // 일 근무 시간 점수
        double score2 = calculateWorkScore2(work.getWeekWorkingDays()); // 주 출근 횟수 점수
        double score3 = calculateWorkScore3(work.getWorkStress()); // 업무 스트레스 점수
        double basicScore = score1 * (0.35) + score2 * (0.2) + score3 * (0.45);
        log.info("work basic score: {}", basicScore);
        int result = globalService.applySatisfaction(basicScore, work.getSatisfaction(), Label.WORK);
        log.info("work result score: {}", result);
        return result;
    }

    private double calculateWorkScore1(double hours) {
        if (hours <= 8) {
            return (hours / 8) * 100;
        } else if (hours <= 9) {
            return 100;
        } else {
            return 100 - ((hours - 9) / 15) * 100;
        }
    }

    private double calculateWorkScore2(int days) {
        return (days < 1) ? 0 :
                (days <= 2) ? 60 :
                (days == 3) ? 80 :
                (days <= 5) ? 100 :
                (days == 6) ? 20 :
                0;
    }

    private double calculateWorkScore3(int stress) {
        if (stress < 1 || stress > 9) {
            throw new IllegalArgumentException("stress 는 1 이상 9 이하의 정수여야 합니다.");
        }
        return (double) (stress - 1) / 8 * 100;
    }
}
