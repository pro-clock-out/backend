package com.hexcode.pro_clock_out.member.dto;

import com.hexcode.pro_clock_out.member.domain.LifeStyle;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UpdateProfileData {
    private String nickname;
    private String photoUrl;
    private List<LifeStyle> life;

    public static UpdateProfileData createWith(UpdateProfileRequest request) {
        return UpdateProfileData.builder()
                .nickname(request.getNickname())
                .photoUrl(request.getPhotoUrl())
                .life(request.getLife())
                .build();
    }
}
