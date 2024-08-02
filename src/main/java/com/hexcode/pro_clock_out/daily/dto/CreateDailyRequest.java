package com.hexcode.pro_clock_out.daily.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateDailyRequest {
    private Date date;
    private int workSatisfaction;
    private int restSatisfaction;
    private int sleepSatisfaction;
    private int personalSatisfaction;
    private int healthSatisfaction;
    private String content;
    private String imageUrl;
    private List<String> completedGoals;
}
