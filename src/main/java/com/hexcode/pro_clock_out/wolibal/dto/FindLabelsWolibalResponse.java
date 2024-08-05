package com.hexcode.pro_clock_out.wolibal.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hexcode.pro_clock_out.global.dto.ResponseDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FindLabelsWolibalResponse implements ResponseDto {
    private Long memberId;

    private int workScore;
    private int workRank;
    private int workAvg;

    private int restScore;
    private int restRank;
    private int restAvg;

    private int sleepScore;
    private int sleepRank;
    private int sleepAvg;

    private int personalScore;
    private int personalRank;
    private int personalAvg;

    private int healthScore;
    private int healthRank;
    private int healthAvg;

    public static FindLabelsWolibalResponse createWith(Long memberId,
                                                       FindScoreRankAvgResponse workDto,
                                                       FindScoreRankAvgResponse restDto,
                                                       FindScoreRankAvgResponse sleepDto,
                                                       FindScoreRankAvgResponse personalDto,
                                                       FindScoreRankAvgResponse healthDto) {
        return FindLabelsWolibalResponse.builder()
                .memberId(memberId)
                .workScore(workDto.getScore())
                .workRank(workDto.getRank())
                .workAvg(workDto.getAvg())
                .restScore(restDto.getScore())
                .restRank(restDto.getRank())
                .restAvg(restDto.getAvg())
                .sleepScore(sleepDto.getScore())
                .sleepRank(sleepDto.getRank())
                .sleepAvg(sleepDto.getAvg())
                .personalScore(personalDto.getScore())
                .personalRank(personalDto.getRank())
                .personalAvg(personalDto.getAvg())
                .healthScore(healthDto.getScore())
                .healthRank(healthDto.getRank())
                .healthAvg(healthDto.getAvg())
                .build();
    }
}
