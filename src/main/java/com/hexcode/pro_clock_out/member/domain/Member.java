package com.hexcode.pro_clock_out.member.domain;

import com.hexcode.pro_clock_out.global.domain.BaseTime;
import com.hexcode.pro_clock_out.member.dto.UpdateProfileData;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Entity
@Getter @Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @NotEmpty
    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @NotEmpty
    private String role;

    private String nickname;

    private String photoUrl;

    @ElementCollection(targetClass = LifeStyle.class)
    @CollectionTable(name = "member_life_styles", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "life_style")
    @Enumerated(EnumType.STRING)
    private List<LifeStyle> life;

    public void updateProfile(UpdateProfileData data) {
        if (data.getLife().size() > 5) {
            throw new IllegalArgumentException("최대 5개의 라이프스타일만 설정할 수 있습니다.");
        }
        this.nickname = data.getNickname();
        this.photoUrl = data.getPhotoUrl();
        this.life = data.getLife();
    }
}
