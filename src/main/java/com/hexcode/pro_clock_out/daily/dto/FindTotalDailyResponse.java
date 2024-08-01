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
        private Date date;
        private Satisfaction satisfaction;
    }

    public static FindTotalDailyResponse createWith(List<Daily> dailyList) {
        List<DailyDetail> dailyDetails = dailyList.stream()
                .map(daily -> DailyDetail.builder()
                        .dailyId(daily.getId())
                        .date(daily.getDate())
                        .satisfaction(daily.getSatisfaction())
                        .build())
                .collect(Collectors.toList());
        return FindTotalDailyResponse.builder()
                .dailyList(dailyDetails)
                .build();
    }
}
