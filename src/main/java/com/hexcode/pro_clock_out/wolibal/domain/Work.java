package com.hexcode.pro_clock_out.wolibal.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hexcode.pro_clock_out.global.domain.BaseTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Getter @Setter @Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Work extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_id")
    private Long id;

    @Min(0) @Max(100)
    private int score;

    @Min(1) @Max(9)
    private int satisfaction;

    @Min(0) @Max(24)
    private double dayWorkingHours;

    @Min(0) @Max(7)
    private int weekWorkingDays;

    @Min(1) @Max(9)
    private int workStress;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "wolibal_id")
    private Wolibal wolibal;
}
