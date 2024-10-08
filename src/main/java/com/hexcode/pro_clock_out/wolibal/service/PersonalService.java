package com.hexcode.pro_clock_out.wolibal.service;

import com.hexcode.pro_clock_out.global.domain.Label;
import com.hexcode.pro_clock_out.global.service.GlobalService;
import com.hexcode.pro_clock_out.wolibal.domain.Personal;
import com.hexcode.pro_clock_out.wolibal.domain.Personal;
import com.hexcode.pro_clock_out.wolibal.domain.Wolibal;
import com.hexcode.pro_clock_out.wolibal.dto.UpdatePersonalRequest;
import com.hexcode.pro_clock_out.wolibal.exception.PersonalNotFoundException;
import com.hexcode.pro_clock_out.wolibal.repository.PersonalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PersonalService {
    private final GlobalService globalService;
    private final PersonalRepository personalRepository;

    public void initializePersonal(Wolibal wolibal) {
        Personal personal = Personal.builder()
                .wolibal(wolibal)
                .build();
        personalRepository.save(personal);
        wolibal.setPersonal(personal);
    }

    public Personal findPersonalById(Long personalId) {
        return personalRepository.findById(personalId)
                .orElseThrow(() -> new PersonalNotFoundException(personalId));
    }

    public Personal findPersonalByWolibal(Wolibal wolibal) {
        return personalRepository.findByWolibal(wolibal)
                .orElseThrow(() -> new PersonalNotFoundException(wolibal));
    }

    public void createAutoPersonal(Wolibal yesterdayWolibal, Wolibal todayWolibal) {
        Personal yesterdayPersonal = findPersonalByWolibal(yesterdayWolibal);
        Personal todayPersonal = Personal.builder()
                .score(yesterdayPersonal.getScore())
                .togetherTime(yesterdayPersonal.getTogetherTime())
                .hobbyTime(yesterdayPersonal.getHobbyTime())
                .satisfaction(yesterdayPersonal.getSatisfaction())
                .wolibal(todayWolibal)
                .build();
        personalRepository.save(todayPersonal);
    }

    public void updatePersonalBySatisfaction(Wolibal wolibal, int satisfaction) {
        Personal personal = findPersonalByWolibal(wolibal);
        personal.setSatisfaction(satisfaction);
        personal.setScore(globalService.applySatisfaction(personal.getScore(), satisfaction, Label.PERSONAL));
    }

    public void updatePersonalByData(Personal personal, UpdatePersonalRequest dto) {
        personal.setTogetherTime(dto.getTogetherTime());
        personal.setHobbyTime(dto.getHobbyTime());
        personal.setSatisfaction(dto.getPersonalSatisfaction());

        personal.setScore(generatePersonalScore(personal));
        personalRepository.save(personal);
    }

    public String describePersonalData(Wolibal wolibal) {
        Personal personal = findPersonalByWolibal(wolibal);
        Integer togetherTime = personal.getTogetherTime();
        Integer hobbyTime = personal.getHobbyTime();
        Integer satisfaction = personal.getSatisfaction();

        StringBuilder result = new StringBuilder();
        if (togetherTime != null) { result.append(String.format("사용자는 친구 및 가족과 보내는 시간을 %d점으로 책정했다. 1일수록 매우 적은 시간을 보내는 것이고, 9일수록 충분한 시간을 보내는 것이다.\n", togetherTime)); }
        if (hobbyTime != null) { result.append(String.format("사용자는 평소에 취미 활동에 보내는 시간을 %d점으로 책정했다. 1일수록 매우 적은 시간을 보내는 것이고, 9일수록 충분한 시간을 보내는 것이다.\n", hobbyTime)); }
        if (satisfaction != null) { result.append(String.format("사용자의 개인 생활에 대한 워라밸 만족도는 %d점이다. 1일수록 매우 불만족이고, 9일수록 매우 만족이다.\n", satisfaction)); }
        return result.toString();
    }

    /**
     * 개인 생활 점수 계산 ///////////////////////////////////////////////////
     */
    private int generatePersonalScore(Personal personal) {
        double score1 = calculatePersonalScore1(personal.getTogetherTime()); // 함께하는 시간 점수
        double score2 = calculatePersonalScore2(personal.getHobbyTime()); // 취미 활동 시간 점수
        double basicScore = (score1 + score2) / 2;
        return globalService.applySatisfaction(basicScore, personal.getSatisfaction(), Label.PERSONAL);
    }

    // 함께하는 시간 점수
    private double calculatePersonalScore1(int togetherTime) {
        if (togetherTime < 1 || togetherTime > 9) {
            throw new IllegalArgumentException("togetherTime 은 1 이상 9 이하의 정수여야 합니다.");
        }
        return (double) (togetherTime - 1) / 8 * 100;
    }

    // 취미 활동 시간 점수
    private double calculatePersonalScore2(int hobbyTime) {
        if (hobbyTime < 1 || hobbyTime > 9) {
            throw new IllegalArgumentException("hobbyTime 은 1 이상 9 이하의 정수여야 합니다.");
        }
        return (double) (hobbyTime - 1) / 8 * 100;
    }
}