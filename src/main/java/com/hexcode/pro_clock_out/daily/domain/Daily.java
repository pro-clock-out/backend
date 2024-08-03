package com.hexcode.pro_clock_out.daily.domain;

import com.hexcode.pro_clock_out.daily.dto.UpdateDailyData;
import com.hexcode.pro_clock_out.global.domain.BaseTime;
import com.hexcode.pro_clock_out.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter @Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Daily extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_id")
    private Long id;

    private LocalDate date;

    private int workSatisfaction;
    private int restSatisfaction;
    private int sleepSatisfaction;
    private int personalSatisfaction;
    private int healthSatisfaction;

    private String content;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public void updateDaily(UpdateDailyData data) {
        this.workSatisfaction = data.getWorkSatisfaction();
        this.restSatisfaction = data.getRestSatisfaction();
        this.sleepSatisfaction = data.getSleepSatisfaction();
        this.personalSatisfaction = data.getPersonalSatisfaction();
        this.healthSatisfaction = data.getHealthSatisfaction();
        this.content = data.getContent();
        this.imageUrl =  data.getImageUrl();
    }

}
