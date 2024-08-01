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
public class CreateDailyResponse implements ResponseDto {
    private Long dailyId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CreateDailyResponse createWith(Daily daily) {
        return CreateDailyResponse.builder()
                .dailyId(daily.getId())
                .createdAt(daily.getCreatedAt())
                .updatedAt(daily.getUpdatedAt())
                .build();
    }
}