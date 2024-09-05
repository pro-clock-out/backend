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
public class Health extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "health_id")
    private Long id;

    @Min(0) @Max(100)
    @Builder.Default
    private Integer score = 0;

    @Min(1) @Max(9)
    @Builder.Default
    private Integer satisfaction = null;

    @Min(0) @Max(7)
    @Builder.Default
    private Integer cardioFrequency = null;

    @Min(0) @Max(24)
    @Builder.Default
    private Double cardioTime = null;

    @Min(0) @Max(7)
    @Builder.Default
    private Integer strengthFrequency = null;

    @Min(0) @Max(24)
    @Builder.Default
    private Double strengthTime = null;

    @Min(1) @Max(9)
    @Builder.Default
    private Integer dietQuality = null;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "wolibal_id", nullable = false)
    private Wolibal wolibal;
}
