package com.hexcode.pro_clock_out.daily.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hexcode.pro_clock_out.daily.domain.Goal;
import com.hexcode.pro_clock_out.global.dto.ResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateGoalResponse implements ResponseDto {
    private Long goalId;

    private LocalDateTime createdAt;

    public static UpdateGoalResponse createWith(Goal goal) {
        return UpdateGoalResponse.builder()
                .goalId(goal.getId())
                .createdAt(goal.getUpdatedAt())
                .build();
    }
}
