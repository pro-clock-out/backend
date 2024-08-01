package com.hexcode.pro_clock_out.daily.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hexcode.pro_clock_out.daily.domain.Satisfaction;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateDailyRequest {
    private Satisfaction satisfaction;
    private String content;
    private String imageUrl;
    private List<String> completedGoals;
}
