package com.hexcode.pro_clock_out.wolibal.domain;

import com.hexcode.pro_clock_out.global.domain.BaseTime;
import com.hexcode.pro_clock_out.member.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.Date;

@Entity
@Getter @Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wolibal extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wolibal_id")
    private Long id;

    @Min(0) @Max(100)
    private int score;

    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private void updateScore(int score) {
        this.score = score;
    }
}
