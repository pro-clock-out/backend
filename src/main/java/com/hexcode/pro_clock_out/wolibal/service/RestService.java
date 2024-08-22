package com.hexcode.pro_clock_out.wolibal.service;

import com.hexcode.pro_clock_out.wolibal.domain.Rest;
import com.hexcode.pro_clock_out.wolibal.domain.Wolibal;
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
    private final RestRepository restRepository;

    public void createAutoRest(Wolibal wolibal) {
        Rest rest = Rest.builder()
                .wolibal(wolibal)
                .build();
        restRepository.save(rest);
    }
}
