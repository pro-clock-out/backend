package com.hexcode.pro_clock_out.calendar.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hexcode.pro_clock_out.calendar.domain.Calendar;
import com.hexcode.pro_clock_out.global.dto.ResponseDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DeleteCalendarResponse implements ResponseDto {
    private Long calendarId;

    public static DeleteCalendarResponse createWith(Calendar calendar) {
        return DeleteCalendarResponse.builder()
                .calendarId(calendar.getId())
                .build();
    }
}
