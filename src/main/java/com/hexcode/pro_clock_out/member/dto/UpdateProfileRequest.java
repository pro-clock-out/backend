package com.hexcode.pro_clock_out.member.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class UpdateProfileRequest {
    private String nickname;

    private String photoUrl;

    @Size(max = 20)
    private String life;
}
