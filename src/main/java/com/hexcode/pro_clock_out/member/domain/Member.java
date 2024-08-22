package com.hexcode.pro_clock_out.member.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hexcode.pro_clock_out.global.domain.BaseTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.ArrayList;
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

    @Column(length = 1000)
    private String photoUrl;

    @ElementCollection(targetClass = Lifestyle.class)
    @CollectionTable(name = "member_lifestyles", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "lifestyle")
    @Enumerated(EnumType.STRING)
    private List<Lifestyle> life;

    @Enumerated(EnumType.STRING)
    private Prefix prefix;

    private String suggestion;

    @ElementCollection
    @JsonIgnore
    private List<String> suggestions = new ArrayList<>();

    private String suggestionString;

    public void addSuggestion(String message) {
        if (!this.suggestions.contains(message)) {
            this.suggestions.add(message);
        }
    }

    public void removeSuggestion(String message) {
        this.suggestions.remove(message);
    }

    public String getSuggestionString() {
        return String.join("\\n", this.suggestions);
    }

    public void updateSuggestionString() {
        getSuggestionString();
    }

    @PrePersist
    protected void onCreate() {
        this.nickname = "hexcode";
        this.prefix = Prefix.NORMAL;
    }

    public void updatePhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateLife(List<Lifestyle> life) {
        this.life = life;
    }

    public void updatePrefix(Prefix prefix) {
        this.prefix = prefix;
    }
}
