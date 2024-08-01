package com.hexcode.pro_clock_out.wolibal.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hexcode.pro_clock_out.global.domain.BaseTime;
import com.hexcode.pro_clock_out.member.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDate;
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

    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToOne(mappedBy = "wolibal", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    private Work work;

    @OneToOne(mappedBy = "wolibal", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    private Rest rest;

    @OneToOne(mappedBy = "wolibal", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    private Sleep sleep;

    @OneToOne(mappedBy = "wolibal", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    private Personal personal;

    @OneToOne(mappedBy = "wolibal", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    private Health health;

    public void updateScore(int score) {
        this.score = score;
    }
}
