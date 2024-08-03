package com.hexcode.pro_clock_out.daily.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hexcode.pro_clock_out.daily.domain.Daily;
import com.hexcode.pro_clock_out.global.dto.ResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateDailyResponse implements ResponseDto {
    private Long dailyId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UpdateDailyResponse createWith(Daily daily) {
        return UpdateDailyResponse.builder()
                .dailyId(daily.getId())
                .createdAt(daily.getCreatedAt())
                .updatedAt(daily.getUpdatedAt())
                .build();
    }
}