package com.hexcode.pro_clock_out.wolibal.service;

import com.hexcode.pro_clock_out.wolibal.domain.Personal;
import com.hexcode.pro_clock_out.wolibal.domain.Wolibal;
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
    private final PersonalRepository personalRepository;

    public void createAutoPersonal(Wolibal wolibal) {
        Personal personal = Personal.builder()
                .wolibal(wolibal)
                .build();
        personalRepository.save(personal);
    }
}