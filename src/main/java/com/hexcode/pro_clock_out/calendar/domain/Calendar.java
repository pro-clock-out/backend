package com.hexcode.pro_clock_out.calendar.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    //private String label;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private String notes;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Label> labels = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    public enum Label {
        작업,
        휴식,
        수면,
        개인생활,
        건강
    }

}
