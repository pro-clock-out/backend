package com.hexcode.pro_clock_out.daily.domain;

import com.hexcode.pro_clock_out.global.domain.BaseTime;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter @Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyGoal extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_goal_id")
    private Long id;

    private Long daily_id;

    private Long goal_id;

    @ManyToOne
    @JoinColumn(name = "daily_id")
    private Daily daily;

    @ManyToOne
    @JoinColumn(name = "goal_id")
    private Goal goal;
}
