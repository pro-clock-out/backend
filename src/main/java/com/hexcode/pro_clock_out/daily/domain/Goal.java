package com.hexcode.pro_clock_out.daily.domain;

import com.hexcode.pro_clock_out.calendar.domain.Label;
import com.hexcode.pro_clock_out.global.domain.BaseTime;
import com.hexcode.pro_clock_out.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter @Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Goal extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Label color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
}