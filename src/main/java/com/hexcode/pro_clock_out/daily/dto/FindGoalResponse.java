package com.hexcode.pro_clock_out.daily.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hexcode.pro_clock_out.daily.domain.Color;
import com.hexcode.pro_clock_out.daily.domain.Goal;
import com.hexcode.pro_clock_out.global.dto.ResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FindGoalResponse implements ResponseDto {
    private Long goalId;
    private String name;
    private Color color;

    public static FindGoalResponse createWith(Goal goal) {
        return FindGoalResponse.builder()
                .goalId(goal.getId())
                .name(goal.getTodo())
                .color(goal.getColor())
                .build();
    }
}
