package com.hexcode.pro_clock_out.wolibal.service;

import com.hexcode.pro_clock_out.global.domain.Label;
import com.hexcode.pro_clock_out.global.service.GlobalService;
import com.hexcode.pro_clock_out.wolibal.domain.Rest;
import com.hexcode.pro_clock_out.wolibal.domain.Wolibal;
import com.hexcode.pro_clock_out.wolibal.domain.Work;
import com.hexcode.pro_clock_out.wolibal.dto.UpdateRestRequest;
import com.hexcode.pro_clock_out.wolibal.exception.RestNotFoundException;
import com.hexcode.pro_clock_out.wolibal.repository.RestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RestService {
    private final GlobalService globalService;
    private final RestRepository restRepository;

    public void initializeRest(Wolibal wolibal) {
        Rest rest = Rest.builder()
                .wolibal(wolibal)
                .build();
        restRepository.save(rest);
        wolibal.setRest(rest);
    }

    public Rest findRestById(Long restId) {
        return restRepository.findById(restId)
                .orElseThrow(() -> new RestNotFoundException(restId));
    }

    public Rest findRestByWolibal(Wolibal wolibal) {
        return restRepository.findByWolibal(wolibal)
                .orElseThrow(() -> new RestNotFoundException(wolibal));
    }

    public void createAutoRest(Wolibal yesterdayWolibal, Wolibal todayWolibal) {
        Rest yesterdayRest = findRestByWolibal(yesterdayWolibal);
        Rest todayRest = Rest.builder()
                .score(yesterdayRest.getScore())
                .workdayRest(yesterdayRest.getWorkdayRest())
                .dayoffRest(yesterdayRest.getDayoffRest())
                .satisfaction(yesterdayRest.getSatisfaction())
                .wolibal(todayWolibal)
                .build();
        restRepository.save(todayRest);
    }

    public void updateRestBySatisfaction(Wolibal wolibal, int satisfaction) {
        Rest rest = findRestByWolibal(wolibal);
        rest.setSatisfaction(satisfaction);
        rest.setScore(globalService.applySatisfaction(rest.getScore(), satisfaction, Label.REST));
    }

    public void updateRestByData(Rest rest, UpdateRestRequest dto) {
        rest.setWorkdayRest(dto.getWorkdayRest());
        rest.setDayoffRest(dto.getDayoffRest());
        rest.setSatisfaction(dto.getRestSatisfaction());

        rest.setScore(generateRestScore(rest));
        restRepository.save(rest);
    }

    public String describeRestData(Wolibal wolibal) {
        Rest rest = findRestByWolibal(wolibal);
        Double workdayRest = rest.getWorkdayRest();
        Double dayoffRest = rest.getDayoffRest();
        Integer satisfaction = rest.getSatisfaction();

        StringBuilder result = new StringBuilder();
        if (workdayRest != null) { result.append(String.format("사용자는 근무일에 %.1f시간 휴식을 취한다.\n", workdayRest)); }
        if (dayoffRest != null) { result.append(String.format("사용자는 휴무일에 %.1f시간 휴식을 취한다.\n", dayoffRest)); }
        if (satisfaction != null) { result.append(String.format("사용자의 휴식에 대한 워라밸 만족도는 %d점이다. 1일수록 매우 불만족이고, 9일수록 매우 만족이다.\n", satisfaction)); }
        return result.toString();
    }

    /**
     * 휴식 점수 계산 ///////////////////////////////////////////////////
     */
    private int generateRestScore(Rest rest) {
        double score1 = calculateRestScore1(rest.getWorkdayRest()); // 근무일 휴식 시간 점수
        double score2 = calculateRestScore2(rest.getDayoffRest()); // 휴무일 휴식 시간 점수
        double basicScore = score1 * (0.4) + score2 * (0.6);
        return globalService.applySatisfaction(basicScore, rest.getSatisfaction(), Label.REST);
    }

    // 근무일 휴식 시간 점수
    private double calculateRestScore1(double hours) {
        if (hours <= 2) {
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
    private double calculateRestScore2(double hours) {
        if (hours <= 6) {
            return hours / 6 * 100;
        } else if (hours <= 8) {
            return 100;
        } else {
            return 100 - ((hours - 8) / 16) * 100;
        }
    }
}
