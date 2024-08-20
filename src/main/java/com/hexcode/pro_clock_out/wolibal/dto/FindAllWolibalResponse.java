package com.hexcode.pro_clock_out.wolibal.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hexcode.pro_clock_out.global.dto.ResponseDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FindAllWolibalResponse implements ResponseDto {
    private FindScoreRankAvgResponse total;
    private FindScoreRankAvgResponse work;
    private FindScoreRankAvgResponse rest;
    private FindScoreRankAvgResponse sleep;
    private FindScoreRankAvgResponse personal;
    private FindScoreRankAvgResponse health;
}
