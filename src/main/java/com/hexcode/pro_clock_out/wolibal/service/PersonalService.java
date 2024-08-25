package com.hexcode.pro_clock_out.wolibal.service;

import com.hexcode.pro_clock_out.global.domain.Label;
import com.hexcode.pro_clock_out.global.service.GlobalService;
import com.hexcode.pro_clock_out.wolibal.domain.Personal;
import com.hexcode.pro_clock_out.wolibal.domain.Wolibal;
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
}