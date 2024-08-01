package com.hexcode.pro_clock_out.daily.dto;

import com.hexcode.pro_clock_out.daily.domain.DailyGoal;
import com.hexcode.pro_clock_out.daily.domain.Goal;
import com.hexcode.pro_clock_out.daily.domain.Satisfaction;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UpdateDailyData {
    private Satisfaction satisfaction;
    private String content;
    private String imageUrl;
    private List<Goal> completedGoal;

    public static UpdateDailyData createWith(UpdateDailyRequest request) {
        return UpdateDailyData.builder()
                .satisfaction(request.getSatisfaction())
                .content(request.getContent())
                .imageUrl(request.getImageUrl())
                .completedGoal(request.getCompletedGoal())
                .build();
    }

}
