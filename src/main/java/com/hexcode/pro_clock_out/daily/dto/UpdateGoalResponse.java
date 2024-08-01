package com.hexcode.pro_clock_out.daily.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hexcode.pro_clock_out.daily.domain.Goal;
import com.hexcode.pro_clock_out.global.dto.ResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UpdateGoalResponse implements ResponseDto {
    private LocalDateTime createdAt;

    public static UpdateGoalResponse createWith(Goal goal) {
        return UpdateGoalResponse.builder()
                .createdAt(goal.getUpdatedAt())
                .build();
    }
}
