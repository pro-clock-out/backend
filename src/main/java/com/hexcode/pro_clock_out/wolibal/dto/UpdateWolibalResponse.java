package com.hexcode.pro_clock_out.wolibal.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hexcode.pro_clock_out.global.dto.ResponseDto;
import com.hexcode.pro_clock_out.wolibal.domain.Wolibal;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateWolibalResponse implements ResponseDto {
    private Long wolibalId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UpdateWolibalResponse createWith(Wolibal wolibal) {
        return UpdateWolibalResponse.builder()
                .wolibalId(wolibal.getId())
                .createdAt(wolibal.getCreatedAt())
                .updatedAt(wolibal.getUpdatedAt())
                .build();
    }
}

