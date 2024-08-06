package com.hexcode.pro_clock_out.daily.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hexcode.pro_clock_out.calendar.domain.Label;
import com.hexcode.pro_clock_out.daily.domain.Goal;
import com.hexcode.pro_clock_out.global.dto.ResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FindGoalResponse implements ResponseDto {

    private List<GoalDetail> goals;

    @Getter
    @Builder
    public static class GoalDetail {
        private Long goalId;
        private String content;
        private Label category;
    }

    public static FindGoalResponse createWith(List<Goal> goals) {
        List<GoalDetail> goalDetails = goals.stream()
                .map(goal -> GoalDetail.builder()
                        .goalId(goal.getId())
                        .content(goal.getContent())
                        .category(goal.getCategory())
                        .build())
                .collect(Collectors.toList());

        return FindGoalResponse.builder()
                .goals(goalDetails)
                .build();
    }
}

