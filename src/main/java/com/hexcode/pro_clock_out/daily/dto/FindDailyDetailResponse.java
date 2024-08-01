package com.hexcode.pro_clock_out.daily.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hexcode.pro_clock_out.daily.domain.Daily;
import com.hexcode.pro_clock_out.daily.domain.Satisfaction;
import com.hexcode.pro_clock_out.global.dto.ResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FindDailyDetailResponse implements ResponseDto {
    private Long dailyId;
    private Date date;
    private Satisfaction satisfaction;
    private String content;
    private String imageUrl;
    private List<String> completedGoals;

    public static FindDailyDetailResponse createWith(Daily daily, List<String> completedGoals) {
        return FindDailyDetailResponse.builder()
                .dailyId(daily.getId())
                .date(daily.getDate())
                .satisfaction(daily.getSatisfaction())
                .content(daily.getContent())
                .imageUrl(daily.getImageUrl())
                .completedGoals(completedGoals)
                .build();
    }
}
