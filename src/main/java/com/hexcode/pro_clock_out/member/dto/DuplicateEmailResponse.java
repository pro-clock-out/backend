package com.hexcode.pro_clock_out.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hexcode.pro_clock_out.global.dto.ResponseDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DuplicateEmailResponse implements ResponseDto {
    private boolean isDuplicated;

    public static DuplicateEmailResponse createWith(boolean isDuplicated) {
        return DuplicateEmailResponse.builder()
                .isDuplicated(isDuplicated)
                .build();
    }
}
