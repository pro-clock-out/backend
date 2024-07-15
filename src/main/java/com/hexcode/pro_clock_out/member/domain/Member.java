package com.hexcode.pro_clock_out.member.domain;

import com.hexcode.pro_clock_out.global.domain.BaseTime;
import com.hexcode.pro_clock_out.member.dto.UpdateProfileData;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity
@Getter @Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @NotEmpty
    @Column(unique = true, nullable = false)
    private String nickname;

    private String photoUrl;

    @Column(length = 20)
    private String life;

    public void updateProfile(UpdateProfileData data) {
        this.nickname = data.getNickname();
        this.photoUrl = data.getPhotoUrl();
        this.life = data.getLife();
    }
}
