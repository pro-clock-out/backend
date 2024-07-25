package com.hexcode.pro_clock_out.daily.domain;

import com.hexcode.pro_clock_out.calendar.dto.UpdateCalendarData;
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

    private Date date;

    @Enumerated(EnumType.STRING)
    private Satisfaction satisfaction;

    private String content;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public void updateDaily(UpdateDailyData data) {
        this.satisfaction = data.getSatisfaction();
        this.content = data.getContent();
        this.imageUrl =  data.getImageUrl();
    }

}
