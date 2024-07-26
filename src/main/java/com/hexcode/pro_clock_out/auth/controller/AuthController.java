package com.hexcode.pro_clock_out.auth.controller;

import com.hexcode.pro_clock_out.auth.dto.JoinDto;
import com.hexcode.pro_clock_out.auth.dto.TokenDto;
import com.hexcode.pro_clock_out.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/hello")
    public String hello() {
        log.info("Hello World~~~!!!");
        return "Hello 퇴근의정석!";
    }

    @PostMapping("/signup")
    public String joinProcess(JoinDto joinDto) {
        log.info(joinDto.getEmail());
        authService.joinProcess(joinDto);
        return joinDto.getEmail();
    }

    @GetMapping("/auth/kakao/login")
    public ResponseEntity<String> kakaoLogin() {
        String kakaoAuthUrl = authService.getKakaoAuthUrl();
        return ResponseEntity.ok(kakaoAuthUrl);
    }

    @GetMapping("/auth/kakao/callback")
    public ResponseEntity<String> kakaoCallback(@RequestParam String code) {
        TokenDto accessToken = authService.getAccessToken(code);
        ResponseEntity<Map<String, Object>> userResponse = authService.getUserInfo(String.valueOf(accessToken));

        // 사용자 정보를 이용해 로그인 처리
        authService.processUserInfo(Objects.requireNonNull(userResponse.getBody()));

        return ResponseEntity.ok("로그인 성공");
    }

    @GetMapping("/auth/kakao/token")
    public TokenDto kakaoAccess(@RequestParam String code){
        return authService.getAccessToken(code);
    }
}
