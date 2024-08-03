package com.hexcode.pro_clock_out.daily.dto;

import com.hexcode.pro_clock_out.daily.domain.Daily;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DailySimpleData {
    private Long dailyId;
    private LocalDate date;
    private int totalScore;

    public static DailySimpleData createWith(Daily daily, int score) {
        return DailySimpleData.builder()
                .dailyId(daily.getId())
                .date(daily.getDate())
                .totalScore(score)
                .build();
    }
}
