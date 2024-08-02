package com.hexcode.pro_clock_out.daily.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hexcode.pro_clock_out.daily.domain.Daily;
import com.hexcode.pro_clock_out.global.dto.ResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FindTotalDailyResponse implements ResponseDto {
    private List<DailyDetail> dailyList;

    @Getter
    @Builder
    public static class DailyDetail {
        private Long dailyId;
        private LocalDate date;
        private int workSatisfaction;
        private int restSatisfaction;
        private int sleepSatisfaction;
        private int personalSatisfaction;
        private int healthSatisfaction;
    }

    public static FindTotalDailyResponse createWith(List<Daily> dailyList) {
        List<DailyDetail> dailyDetails = dailyList.stream()
                .map(daily -> DailyDetail.builder()
                        .dailyId(daily.getId())
                        .date(daily.getDate())
                        .workSatisfaction(daily.getWorkSatisfaction())
                        .restSatisfaction(daily.getRestSatisfaction())
                        .sleepSatisfaction(daily.getSleepSatisfaction())
                        .personalSatisfaction(daily.getPersonalSatisfaction())
                        .healthSatisfaction(daily.getHealthSatisfaction())
                        .build())
                .collect(Collectors.toList());
        return FindTotalDailyResponse.builder()
                .dailyList(dailyDetails)
                .build();
    }
}