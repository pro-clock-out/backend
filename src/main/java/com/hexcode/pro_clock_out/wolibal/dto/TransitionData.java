package com.hexcode.pro_clock_out.wolibal.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class TransitionData {
    private LocalDate date;
    private int score;

    public static TransitionData createWith(LocalDate date, int score) {
        return TransitionData.builder()
                .date(date)
                .score(score)
                .build();
    }
}
