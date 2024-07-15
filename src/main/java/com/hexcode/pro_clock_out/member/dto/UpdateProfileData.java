package com.hexcode.pro_clock_out.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateProfileData {
    private String nickname;
    private String photoUrl;
    private String life;

    public static UpdateProfileData createWith(UpdateProfileRequest request) {
        return UpdateProfileData.builder()
                .nickname(request.getNickname())
                .photoUrl(request.getPhotoUrl())
                .life(request.getLife())
                .build();
    }
}
