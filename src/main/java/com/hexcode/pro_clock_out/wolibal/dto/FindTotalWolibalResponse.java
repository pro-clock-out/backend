package com.hexcode.pro_clock_out.wolibal.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hexcode.pro_clock_out.global.dto.ResponseDto;
import com.hexcode.pro_clock_out.wolibal.domain.Prefix;
import com.hexcode.pro_clock_out.wolibal.domain.Wolibal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FindTotalWolibalResponse implements ResponseDto {
    private Long memberId;
    private Prefix prefix;
    private int totalScore;
    private int totalRank;
    private int totalAvg;

    public static FindTotalWolibalResponse createWith(Wolibal wolibal, WolibalScoreRankAvgDto totalDto) {
        return FindTotalWolibalResponse.builder()
                .memberId(wolibal.getMember().getId())
                .prefix(wolibal.getPrefix())
                .totalScore(totalDto.getScore())
                .totalRank(totalDto.getRank())
                .totalAvg(totalDto.getAvg())
                .build();
    }
}
