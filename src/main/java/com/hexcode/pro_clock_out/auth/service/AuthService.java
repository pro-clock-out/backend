package com.hexcode.pro_clock_out.auth.service;

import com.hexcode.pro_clock_out.auth.dto.JoinDto;
import com.hexcode.pro_clock_out.auth.dto.TokenDto;
import com.hexcode.pro_clock_out.member.domain.Member;
import com.hexcode.pro_clock_out.member.exception.MemberNotFoundException;
import com.hexcode.pro_clock_out.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RestTemplate restTemplate;

    @Value("${kakao.client.id}")
    private String kakaoClientId;

    @Value("${kakao.redirect.uri}")
    private String kakaoRedirectUri;

    @Value("${kakao.client.secret}")
    private String kakaoClientSecret;

    public String getKakaoAuthUrl() {
        return "https://kauth.kakao.com/oauth/authorize?" +
                "client_id=" + kakaoClientId +
                "&redirect_uri=" + kakaoRedirectUri +
                "&response_type=code";
    }

    public TokenDto getAccessToken(String code) {
        String accessTokenUrl = "https://kauth.kakao.com/oauth/token";

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoClientId);
        body.add("redirect_uri", kakaoRedirectUri);
        body.add("code", code);
        body.add("client_secret", kakaoClientSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                accessTokenUrl,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        String token = (String) Objects.requireNonNull(response.getBody()).get("access_token");
        return TokenDto.builder().accessToken(token).build();
    }

    public ResponseEntity<Map<String, Object>> getUserInfo(String accessToken) {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> userRequest = new HttpEntity<>(userHeaders);

        return restTemplate.exchange(
                userInfoUrl,
                HttpMethod.GET,
                userRequest,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );
    }

    public void processUserInfo(Map<String, Object> userInfo) {
        Map<String, Object> kakaoAccount = getMapFromObject(userInfo.get("kakao_account"));

        if (kakaoAccount == null) {
            log.error("Kakao account information is missing or invalid.");
            return;
        }
        String email = (String) kakaoAccount.get("email");

        registerOrLoginMember(email, "defaultPassword");
    }

    private Map<String, Object> getMapFromObject(Object obj) {
        if (obj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) obj;
            return map;
        }
        return null;
    }

    public void joinProcess(JoinDto joinDto) {
        String email = joinDto.getEmail();
        String password = joinDto.getPassword();

        registerOrLoginMember(email, password);
    }

    private void registerOrLoginMember(String email, String password) {
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        Member existingMember = memberRepository.findByEmail(email).orElse(null);

        if (existingMember == null) {
            // 회원가입 처리
            Member newMember = Member.builder()
                    .email(email)
                    .password(encodedPassword)
                    .role("ROLE_USER") // 역할 설정
                    .build();
            memberRepository.save(newMember);
            log.info("새로운 사용자 회원가입: {}", email);
        } else {
            log.info("기존 사용자 로그인: {}", email);
        }
    }
}