package com.hexcode.pro_clock_out.wolibal.controller;

import com.hexcode.pro_clock_out.auth.dto.CustomUserDetails;
import com.hexcode.pro_clock_out.global.dto.ResponseDto;
import com.hexcode.pro_clock_out.wolibal.dto.*;
import com.hexcode.pro_clock_out.wolibal.service.WolibalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WolibalController {
    private final WolibalService wolibalService;

//    @GetMapping("members/me/wolibals/total")
//    public ResponseEntity<ResponseDto> getTotalWolibal(Authentication authentication, @RequestParam("option") String option) {
//        log.info("Request to get total wolibal");
//        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
//        FindTotalWolibalResponse response = wolibalService.findTotalWolibal(memberId, option);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    @GetMapping("/members/me/wolibals/label")
//    public ResponseEntity<ResponseDto> getLabelsWolibal(Authentication authentication, @RequestParam("option") String option) {
//        log.info("Request to get labels wolibal");
//        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
//        FindLabelsWolibalResponse response = wolibalService.findLabelsWolibal(memberId, option);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

    @PostMapping("/members/me/wolibals/work")
    public ResponseEntity<ResponseDto> postWork(Authentication authentication, @RequestBody CreateWorkRequest request) {
        log.info("Request to post work wolibal");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        CreateWolibalResponse response = wolibalService.createWork(memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/members/me/wolibals/rest")
    public ResponseEntity<ResponseDto> postRest(Authentication authentication, @RequestBody CreateRestRequest request) {
        log.info("Request to post rest wolibal");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        CreateWolibalResponse response = wolibalService.createRest(memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/members/me/wolibals/sleep")
    public ResponseEntity<ResponseDto> postSleep(Authentication authentication, @RequestBody CreateSleepRequest request) {
        log.info("Request to post sleep wolibal");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        CreateWolibalResponse response = wolibalService.createSleep(memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/members/me/wolibals/personal")
    public ResponseEntity<ResponseDto> postPersonal(Authentication authentication, @RequestBody CreatePersonalRequest request) {
        log.info("Request to post personal wolibal");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        CreateWolibalResponse response = wolibalService.createPersonal(memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/members/me/wolibals/health")
    public ResponseEntity<ResponseDto> postHealth(Authentication authentication, @RequestBody CreateHealthRequest request) {
        log.info("Request to post heath wolibal");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        CreateWolibalResponse response = wolibalService.createHealth(memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
