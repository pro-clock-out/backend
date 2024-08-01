package com.hexcode.pro_clock_out.calendar.dto;

import com.hexcode.pro_clock_out.calendar.domain.Label;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
public class UpdateCalendarRequest {
    private Label label;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;

    @Size(max = 300)
    private String notes;
}
