package com.hexcode.pro_clock_out.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexcode.pro_clock_out.auth.dto.JoinDto;
import com.hexcode.pro_clock_out.auth.dto.TokenDto;
import com.hexcode.pro_clock_out.auth.jwt.JwtUtil;
import com.hexcode.pro_clock_out.member.domain.Member;
import com.hexcode.pro_clock_out.member.exception.MemberNotFoundException;
import com.hexcode.pro_clock_out.member.repository.MemberRepository;
import com.hexcode.pro_clock_out.member.service.MemberService;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;

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

    public String getAccessToken(String code) {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoClientId);
        body.add("redirect_uri", kakaoRedirectUri);
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            log.error("getAccessToken 에서 json 토큰 파싱 오류: ", e);
        }
        assert jsonNode != null;
        return jsonNode.get("access_token").asText(); //토큰 전송
    }

    public HashMap<String, Object> getKakaoUserInfo(String accessToken) {
        HashMap<String, Object> userInfo= new HashMap<String,Object>();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        // responseBody에 있는 정보를 꺼냄
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            log.error("getKakaoUserInfo 에서 json 토큰 파싱 오류: ", e);
        }

        assert jsonNode != null;
        Long id = jsonNode.get("id").asLong();
        String email = jsonNode.get("kakao_account").get("email").asText();
        String nickname = jsonNode.get("properties").get("nickname").asText();

        userInfo.put("id",id);
        userInfo.put("email",email);
        userInfo.put("nickname",nickname);

        return userInfo;
    }

    //3. 카카오ID로 회원가입 & 로그인 처리
    public String kakaoUserLogin(HashMap<String, Object> userInfo){
        String kakaoEmail = userInfo.get("email").toString();
        registerOrLoginMember(kakaoEmail, "kakao");
        Member kakaoUser = memberService.findMemberByEmail(kakaoEmail);
        return "Bearer " + jwtUtil.createJwt(kakaoEmail, kakaoUser.getRole());
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