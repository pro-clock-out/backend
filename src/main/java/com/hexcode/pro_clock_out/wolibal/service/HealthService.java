package com.hexcode.pro_clock_out.wolibal.service;

import com.hexcode.pro_clock_out.wolibal.domain.Health;
import com.hexcode.pro_clock_out.wolibal.domain.Wolibal;
import com.hexcode.pro_clock_out.wolibal.repository.HealthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class HealthService {
    private final HealthRepository healthRepository;

    public void createAutoHealth(Wolibal wolibal) {
        Health health = Health.builder()
                .wolibal(wolibal)
                .build();
        healthRepository.save(health);
    }
}
