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
public class Personal extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "personal_id")
    private Long id;

    @Min(0) @Max(100)
    private int score;

    @Min(1) @Max(9)
    private int satisfaction;

    @Min(1) @Max(9)
    private int togetherTime;

    @Min(1) @Max(9)
    private int hobbyTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "wolibal_id")
    private Wolibal wolibal;
}
