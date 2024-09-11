package com.hexcode.pro_clock_out.member.controller;

import com.hexcode.pro_clock_out.auth.dto.CustomUserDetails;
import com.hexcode.pro_clock_out.global.dto.ResponseDto;
import com.hexcode.pro_clock_out.global.service.S3Service;
import com.hexcode.pro_clock_out.member.dto.*;
import com.hexcode.pro_clock_out.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberController {
    private final MemberService memberService;
    private final S3Service s3Service;

    @GetMapping("/cheer")
    public ResponseEntity<ResponseDto> getCheer() {
        log.info("Request to GET cheer message");
        FindCheerMessage response = memberService.findCheerMessage();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/members/me/dday")
    public ResponseEntity<ResponseDto> getDDay(Authentication authentication) {
        log.info("Request to GET my d-day");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        FindMyDDayResponse response = memberService.findMyDDay(memberId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/members/me/profile")
    public ResponseEntity<ResponseDto> getProfile(Authentication authentication) {
        log.info("Request to GET profile");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        FindProfileResponse response = memberService.findProfile(memberId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/members/me/profile/image")
    public ResponseEntity<ResponseDto> putProfileImage(Authentication authentication, @RequestParam MultipartFile file) {
        log.info("Request to PUT profile imgae");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        String imageUrl = s3Service.uploadFile(file);
        UpdateProfileResponse response = memberService.updateProfileImage(memberId, imageUrl);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/members/me/profile/nickname")
    public ResponseEntity<ResponseDto> putProfileNickname(Authentication authentication, @RequestBody UpdateNicknameRequest request) {
        log.info("Request to PUT nickname");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        UpdateProfileResponse response = memberService.updateNickname(memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/members/me/profile/lifestyle")
    public ResponseEntity<ResponseDto> putProfileLifestyle(Authentication authentication, @RequestBody UpdateLifestyleRequest request) {
        log.info("Request to PUT lifestyle");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        UpdateProfileResponse response = memberService.updateLifestyle(memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/duplicate/email")
    public ResponseEntity<ResponseDto> duplicateEmail(@RequestBody DuplicateEmailRequest request) {
        log.info("Request to CHECK duplicate email");
        DuplicateEmailResponse response = memberService.hasEmail(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/members/me/solution")
    public ResponseEntity<ResponseDto> suggestSolution(Authentication authentication) {
        log.info("Request to GET solution");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        SuggestSolutionResponse response = memberService.findSuggestionMessage(memberId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
