package com.hexcode.pro_clock_out.calendar.dto;

import com.hexcode.pro_clock_out.calendar.domain.Calendar;
import com.hexcode.pro_clock_out.global.domain.Label;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CalendarEventResponse {
    private Long id;
    private String title;
    private String notes;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Label label;

    public CalendarEventResponse(Calendar calendar) {
        this.id = calendar.getId();
        this.title = calendar.getTitle();
        this.notes = calendar.getNotes();
        this.location = calendar.getLocation();
        this.startTime = calendar.getStartTime();
        this.endTime = calendar.getEndTime();
        this.label = calendar.getLabel();
    }

    public CalendarEventResponse(String title, String notes, String location) {
    }
}
