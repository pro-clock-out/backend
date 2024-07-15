package com.hexcode.pro_clock_out.wolibal.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WolibalScoreRankAvgDto {
    private int score;
    private int rank;
    private int avg;
}
