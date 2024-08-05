package com.hexcode.pro_clock_out.daily.dto;

import com.hexcode.pro_clock_out.calendar.domain.Label;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateGoalData {
    private Long goalId;
    private String name;
    private Label color;

    public static CreateGoalData createWith(CreateGoalRequest request) {
        return CreateGoalData.builder()
                .goalId(request.getGoalId())
                .name(request.getName())
                .color(request.getColor())
                .build();
    }
}
