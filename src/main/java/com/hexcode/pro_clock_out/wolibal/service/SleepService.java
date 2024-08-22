package com.hexcode.pro_clock_out.wolibal.service;

import com.hexcode.pro_clock_out.wolibal.domain.Sleep;
import com.hexcode.pro_clock_out.wolibal.domain.Wolibal;
import com.hexcode.pro_clock_out.wolibal.repository.SleepRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SleepService {
    private final SleepRepository sleepRepository;

    public void createAutoSleep(Wolibal wolibal) {
        Sleep sleep = Sleep.builder()
                .wolibal(wolibal)
                .build();
        sleepRepository.save(sleep);
    }
}