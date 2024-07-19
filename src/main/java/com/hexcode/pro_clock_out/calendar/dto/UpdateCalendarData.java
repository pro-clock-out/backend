package com.hexcode.pro_clock_out.calendar.dto;

import com.hexcode.pro_clock_out.calendar.domain.Label;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UpdateCalendarData {
    private Label label;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private String notes;

    public static UpdateCalendarData createWith(UpdateCalendarRequest request){
        return UpdateCalendarData.builder()
                .label(request.getLabel())
                .title(request.getTitle())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .location(request.getLocation())
                .notes(request.getNotes())
                .build();
    }
}
