package com.hexcode.pro_clock_out.wolibal.service;

import com.hexcode.pro_clock_out.wolibal.domain.Wolibal;
import com.hexcode.pro_clock_out.wolibal.domain.Work;
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
    private final WorkRepository workRepository;

    public void createAutoWork(Wolibal wolibal) {
        Work work = Work.builder()
                .wolibal(wolibal)
                .build();
        workRepository.save(work);
    }
}
