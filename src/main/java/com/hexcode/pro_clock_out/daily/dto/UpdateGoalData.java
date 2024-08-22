package com.hexcode.pro_clock_out.daily.dto;

import com.hexcode.pro_clock_out.global.domain.Label;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateGoalData {
    private String content;
    private Label category;

    public static UpdateGoalData createWith(UpdateGoalRequest request) {
        return UpdateGoalData.builder()
                .content(request.getContent())
                .category(request.getCategory())
                .build();
    }
}