package com.hexcode.pro_clock_out.member.controller;

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
        Long memberId = (Long) authentication.getPrincipal();
        FindMyDDayResponse response = memberService.findMyDDay(memberId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/members/{memberId}/profile")
    public ResponseEntity<ResponseDto> getProfile(@PathVariable("memberId") Long memberId) {
        log.info("Request to get profile");
        FindProfileResponse response = memberService.findProfile(memberId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/members/me/profile")
    public ResponseEntity<ResponseDto> putProfile(Authentication authentication, @RequestBody UpdateProfileRequest request) {
        log.info("Request to put profile");
        Long memberId = (Long) authentication.getPrincipal();
        UpdateProfileResponse response = memberService.updateProfile(memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
