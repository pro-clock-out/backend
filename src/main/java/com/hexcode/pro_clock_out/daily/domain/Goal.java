package com.hexcode.pro_clock_out.daily.domain;

import com.hexcode.pro_clock_out.global.domain.BaseTime;
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

    private String todo;

    private Color color;

}

