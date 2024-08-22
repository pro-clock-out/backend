package com.hexcode.pro_clock_out.daily.dto;

import com.hexcode.pro_clock_out.global.domain.Label;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateGoalData {
    private Long goalId;
    private String content;
    private Label category;

    public static CreateGoalData createWith(CreateGoalRequest request) {
        return CreateGoalData.builder()
                .goalId(request.getGoalId())
                .content(request.getContent())
                .category(request.getCategory())
                .build();
    }
}
