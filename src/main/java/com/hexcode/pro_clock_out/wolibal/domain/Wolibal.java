package com.hexcode.pro_clock_out.wolibal.domain;

import com.hexcode.pro_clock_out.global.domain.BaseTime;
import com.hexcode.pro_clock_out.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wolibal extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Prefix prefix;

    private int total;
    private int work;
    private int rest;
    private int sleep;
    private int personal;
    private int health;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

}
