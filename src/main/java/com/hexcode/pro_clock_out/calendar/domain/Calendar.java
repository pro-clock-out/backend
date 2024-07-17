package com.hexcode.pro_clock_out.calendar.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hexcode.pro_clock_out.calendar.dto.UpdateCalendarData;
import com.hexcode.pro_clock_out.global.domain.BaseTime;
import jakarta.persistence.*;
import lombok.*;
import com.hexcode.pro_clock_out.member.domain.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Calendar extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calendar_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Label label;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private String notes;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    public void updateCalendar(UpdateCalendarData data) {
        this.label = data.getLabel();
        this.title = data.getTitle();
        this.startTime = data.getStartTime();
        this.endTime =  data.getEndTime();
        this.location = data.getLocation();
        this.notes = data.getNotes();
    }

}
