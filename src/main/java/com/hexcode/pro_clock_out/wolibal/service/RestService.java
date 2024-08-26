package com.hexcode.pro_clock_out.wolibal.service;

import com.hexcode.pro_clock_out.global.domain.Label;
import com.hexcode.pro_clock_out.global.service.GlobalService;
import com.hexcode.pro_clock_out.wolibal.domain.Rest;
import com.hexcode.pro_clock_out.wolibal.domain.Wolibal;
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

    public Wolibal updateRestByData(Long restId, UpdateRestRequest dto) {
        Rest rest = findRestById(restId);

        rest.setWorkdayRest(dto.getWorkdayRest());
        rest.setDayoffRest(dto.getDayoffRest());
        rest.setSatisfaction(dto.getRestSatisfaction());

        rest.setScore(generateRestScore(rest));
        restRepository.save(rest);

        return rest.getWolibal();
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
