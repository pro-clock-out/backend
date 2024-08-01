package com.hexcode.pro_clock_out.calendar.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hexcode.pro_clock_out.calendar.domain.Calendar;
import com.hexcode.pro_clock_out.calendar.domain.Label;
import com.hexcode.pro_clock_out.global.dto.ResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FindCalendarDetailResponse implements ResponseDto {
    private Long id;
    private Label label;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private String notes;
    public static FindCalendarDetailResponse createWith(Calendar calendar) {
        return FindCalendarDetailResponse.builder()
                .id(calendar.getId())
                .label(calendar.getLabel())
                .title(calendar.getTitle())
                .startTime(calendar.getStartTime())
                .endTime(calendar.getEndTime())
                .location(calendar.getLocation())
                .notes(calendar.getNotes())
                .build();
    }
}
