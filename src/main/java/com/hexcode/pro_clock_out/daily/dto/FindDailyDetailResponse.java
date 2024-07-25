package com.hexcode.pro_clock_out.daily.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hexcode.pro_clock_out.daily.domain.Daily;
import com.hexcode.pro_clock_out.daily.domain.DailyGoal;
import com.hexcode.pro_clock_out.daily.domain.Satisfaction;
import com.hexcode.pro_clock_out.global.dto.ResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FindDailyDetailResponse implements ResponseDto {
    private Long dailyId;
    private LocalDate date;
    private Satisfaction satisfaction;
    private String content;
    private String imageUrl;
    private List<DailyGoal> completedGoal;

    public static FindDailyDetailResponse createWith(Daily daily, LocalDate date, List<DailyGoal> completedGoal) {
        return FindDailyDetailResponse.builder()
                .dailyId(daily.getId())
                .date(date)
                .satisfaction(daily.getSatisfaction())
                .content(daily.getContent())
                .imageUrl(daily.getImageUrl())
                .completedGoal(completedGoal)
                .build();
    }
}
