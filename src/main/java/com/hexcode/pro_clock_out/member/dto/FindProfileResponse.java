package com.hexcode.pro_clock_out.member.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hexcode.pro_clock_out.global.dto.ResponseDto;
import com.hexcode.pro_clock_out.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FindProfileResponse implements ResponseDto {
    private Long memberId;
    private String email;
    private String username;
    private String photoUrl;
    private String life;

    public static FindProfileResponse createWith(Member member) {
        return FindProfileResponse.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .username(member.getNickname())
                .photoUrl(member.getPhotoUrl())
                .life(member.getLife())
                .build();
    }
}
