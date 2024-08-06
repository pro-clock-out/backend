package com.hexcode.pro_clock_out.daily.domain;

import com.hexcode.pro_clock_out.calendar.domain.Label;
import com.hexcode.pro_clock_out.daily.dto.UpdateDailyData;
import com.hexcode.pro_clock_out.daily.dto.UpdateGoalData;
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

    private String content;

    @Enumerated(EnumType.STRING)
    private Label category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public void updateGoals(UpdateGoalData data) {
        this.content = data.getContent();
        this.category = data.getCategory();
    }
}