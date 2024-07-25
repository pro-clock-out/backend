package com.hexcode.pro_clock_out.daily.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hexcode.pro_clock_out.daily.domain.Daily;
import com.hexcode.pro_clock_out.daily.domain.Satisfaction;
import com.hexcode.pro_clock_out.global.dto.ResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FindTotalDailyResponse implements ResponseDto {
    private Long daily_id;
    private Date date;
    private Satisfaction satisfaction;

    public static FindTotalDailyResponse createWith(Daily daily) {
        return FindTotalDailyResponse.builder()
                .daily_id(daily.getId())
                .date(daily.getDate())
                .satisfaction(daily.getSatisfaction())
                .build();
    }
}