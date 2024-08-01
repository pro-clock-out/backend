package com.hexcode.pro_clock_out.wolibal.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateSleepRequest {
    @Min(0) @Max(24)
    private double workdayBedtime;
    @Min(0) @Max(24)
    private double workdayWakeup;
    @Min(0) @Max(24)
    private double dayoffBedtime;
    @Min(0) @Max(24)
    private double dayoffWakeup;
    @Min(1) @Max(9)
    private int sleepSatisfaction;
}
