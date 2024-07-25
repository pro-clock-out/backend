package com.hexcode.pro_clock_out.daily.dto;

import com.hexcode.pro_clock_out.daily.domain.Color;
import com.hexcode.pro_clock_out.daily.domain.Goal;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UpdateGoalData {
    private Long goalId;
    private String name;
    private Color color;

    public static UpdateGoalData createWith(UpdateGoalRequest request) {
        return UpdateGoalData.builder()
                .goalId(request.getGoalId())
                .name(request.getName())
                .color(request.getColor())
                .build();
    }
}