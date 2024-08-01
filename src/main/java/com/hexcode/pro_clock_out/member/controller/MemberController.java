package com.hexcode.pro_clock_out.member.controller;

import com.hexcode.pro_clock_out.auth.dto.CustomUserDetails;
import com.hexcode.pro_clock_out.global.dto.ResponseDto;
import com.hexcode.pro_clock_out.member.dto.*;
import com.hexcode.pro_clock_out.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberController {
    private final MemberService memberService;


    @GetMapping("/members/me/dday")
    public ResponseEntity<ResponseDto> getDDay(Authentication authentication) {
        log.info("Request to get my d-day");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        FindMyDDayResponse response = memberService.findMyDDay(memberId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/members/{memberId}/profile")
    public ResponseEntity<ResponseDto> getProfile(@PathVariable("memberId") Long memberId) {
        log.info("Request to get profile");
        FindProfileResponse response = memberService.findProfile(memberId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/members/me/profile/image")
    public ResponseEntity<ResponseDto> putProfileImage(Authentication authentication, @RequestBody UpdateProfileImageRequest request) {
        log.info("Request to put profile imgae");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        UpdateProfileResponse response = memberService.updateProfileImage(memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/members/me/profile/nickname")
    public ResponseEntity<ResponseDto> putProfileNickname(Authentication authentication, @RequestBody UpdateNicknameRequest request) {
        log.info("Request to put profile image");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        UpdateProfileResponse response = memberService.updateNickname(memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/members/me/profile/lifestyle")
    public ResponseEntity<ResponseDto> putProfileLifestyle(Authentication authentication, @RequestBody UpdateLifestyleRequest request) {
        log.info("Request to put profile lifestyle");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        UpdateProfileResponse response = memberService.updateLifestyle(memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
