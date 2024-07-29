package com.hexcode.pro_clock_out.wolibal.domain;

import com.hexcode.pro_clock_out.global.domain.BaseTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Health extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "health_id")
    private Long id;

    @Min(0) @Max(100)
    private int score;

    @Min(1) @Max(9)
    private int satisfaction;

    @Min(0) @Max(7)
    private int weekExerciseDays;

    @Min(0) @Max(24)
    private double dayExerciseHours;

    @Min(1) @Max(9)
    private int diet;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wolibal_id", nullable = false)
    private Wolibal wolibal;
}
