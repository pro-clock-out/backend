package com.hexcode.pro_clock_out.daily.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UpdateDailyData {
    private int workSatisfaction;
    private int restSatisfaction;
    private int sleepSatisfaction;
    private int personalSatisfaction;
    private int healthSatisfaction;
    private String content;
    private String imageUrl;
    private List<String> completedGoals;

    public static UpdateDailyData createWith(UpdateDailyRequest request) {
        return UpdateDailyData.builder()
                .workSatisfaction(request.getWorkSatisfaction())
                .restSatisfaction(request.getRestSatisfaction())
                .sleepSatisfaction(request.getSleepSatisfaction())
                .personalSatisfaction(request.getPersonalSatisfaction())
                .healthSatisfaction(request.getHealthSatisfaction())
                .content(request.getContent())
                .imageUrl(request.getImageUrl())
                .completedGoals(request.getCompletedGoals())
                .build();
    }

}
