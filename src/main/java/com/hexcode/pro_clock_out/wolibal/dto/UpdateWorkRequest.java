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
public class UpdateWorkRequest {
    @Min(0) @Max(24)
    private double dayWorkingHours;
    @Min(0) @Max(7)
    private int weekWorkingDays;
    @Min(1) @Max(9)
    private int workStress;
    @Min(1) @Max(9)
    private int workSatisfaction;
}
