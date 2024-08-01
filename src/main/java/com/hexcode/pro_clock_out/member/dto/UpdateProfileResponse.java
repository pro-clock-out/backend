package com.hexcode.pro_clock_out.member.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hexcode.pro_clock_out.global.dto.ResponseDto;
import com.hexcode.pro_clock_out.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateProfileResponse implements ResponseDto {
    private Long memberId;
    private LocalDateTime updatedAt;

    public static UpdateProfileResponse createWith(Member member) {
        return UpdateProfileResponse.builder()
                .memberId(member.getId())
                .updatedAt(member.getUpdatedAt())
                .build();
    }
}
