package com.hexcode.pro_clock_out.daily.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hexcode.pro_clock_out.daily.domain.Daily;
import com.hexcode.pro_clock_out.daily.domain.Satisfaction;
import com.hexcode.pro_clock_out.global.dto.ResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FindTotalDailyResponse implements ResponseDto {
    private Long daily_id;
    private LocalDate date;
    private Satisfaction satisfaction;

    public static FindTotalDailyResponse createWith(Daily daily, LocalDate date) {
        return FindTotalDailyResponse.builder()
                .daily_id(daily.getId())
                .date(date)
                .satisfaction(daily.getSatisfaction())
                .build();
    }
}