package com.hexcode.pro_clock_out.calendar.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hexcode.pro_clock_out.calendar.domain.Calendar;
import com.hexcode.pro_clock_out.global.dto.ResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddCalendarResponse implements ResponseDto {
    private Long calendarId;
    private LocalDateTime updatedAt;

    public static AddCalendarResponse createWith(Calendar calendar) {
        return AddCalendarResponse.builder()
                .calendarId(calendar.getId())
                .updatedAt(calendar.getUpdatedAt())
                .build();
    }
}
