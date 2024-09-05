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
public class Sleep extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sleep_id")
    private Long id;

    @Min(0) @Max(100)
    @Builder.Default
    private Integer score = 0;

    @Min(1) @Max(9)
    @Builder.Default
    private Integer satisfaction = null;

    @Min(12) @Max(48)
    @Builder.Default
    private Double workdayBedtime = null;

    @Min(12) @Max(48)
    @Builder.Default
    private Double workdayWakeup = null;

    @Min(12) @Max(48)
    @Builder.Default
    private Double dayoffBedtime = null;

    @Min(12) @Max(48)
    @Builder.Default
    private Double dayoffWakeup = null;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "wolibal_id", nullable = false)
    private Wolibal wolibal;
}
