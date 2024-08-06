package com.hexcode.pro_clock_out.auth.controller;

import com.hexcode.pro_clock_out.auth.dto.JoinDto;
import com.hexcode.pro_clock_out.auth.dto.LoginRequest;
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

import java.util.HashMap;
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
    public ResponseEntity<String> joinProcess(@RequestBody JoinDto joinDto) {
        log.info("signup email: {}", joinDto.getEmail());
        authService.joinProcess(joinDto);
        return ResponseEntity.ok(joinDto.getEmail());
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        log.info("login email: {}", loginRequest.getEmail());
        return ResponseEntity.ok(loginRequest.getEmail());
    }

    @GetMapping("/oauth/kakao/login")
    public ResponseEntity<String> kakaoLogin() {
        String kakaoAuthUrl = authService.getKakaoAuthUrl();
        return ResponseEntity.ok(kakaoAuthUrl);
    }

    @GetMapping("/oauth/kakao/callback")
    public ResponseEntity<String> kakaoCallback(@RequestParam String code) {
        String accessToken = authService.getAccessToken(code);
        HashMap<String, Object> userResponse = authService.getKakaoUserInfo(accessToken);
        String token = authService.kakaoUserLogin(userResponse);
        return ResponseEntity.ok(token);
    }
}