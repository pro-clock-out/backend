package com.hexcode.pro_clock_out.daily.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hexcode.pro_clock_out.daily.domain.Daily;
import com.hexcode.pro_clock_out.global.dto.ResponseDto;
import com.hexcode.pro_clock_out.member.exception.WolibalNotFoundException;
import com.hexcode.pro_clock_out.wolibal.domain.Wolibal;
import com.hexcode.pro_clock_out.wolibal.repository.WolibalRepository;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FindTotalDailyResponse implements ResponseDto {
    private List<DailySimpleData> dailyList;

    public static FindTotalDailyResponse createWith(List<DailySimpleData> dailyList) {
        return FindTotalDailyResponse.builder()
                .dailyList(dailyList)
                .build();
    }
}