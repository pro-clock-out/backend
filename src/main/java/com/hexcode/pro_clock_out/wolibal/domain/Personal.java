package com.hexcode.pro_clock_out.wolibal.domain;

import com.hexcode.pro_clock_out.global.domain.BaseTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Personal extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "personal_id")
    private Long id;

    private int score;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wolibal_id", nullable = false)
    private Wolibal wolibal;
}
