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

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WolibalController {
    private final WolibalService wolibalService;

    @GetMapping("/wolibals/total")
    public ResponseEntity<ResponseDto> getTotalWolibal(Authentication authentication) {
        log.info("Request to GET total wolibal");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        FindScoreRankAvgResponse response = wolibalService.findTotalWolibal(memberId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/wolibals/all")
    public ResponseEntity<ResponseDto> getLabelsWolibal(Authentication authentication) {
        log.info("Request to get labels wolibal");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        FindAllWolibalResponse response = wolibalService.findAllWolibals(memberId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 워라밸 추이 조회
    @GetMapping("/wolibals/transitions")
    public ResponseEntity<ResponseDto> getWolibalTransition(Authentication authentication) {
        log.info("Request to GET wolibal transitions");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        FindWolibalTransitionsResponse response = wolibalService.findTransitions(memberId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 업데이트
    @PutMapping("/wolibals/work")
    public ResponseEntity<ResponseDto> putWork(Authentication authentication, @RequestBody UpdateWorkRequest request) {
        log.info("Request to PUT work wolibal");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        UpdateWolibalResponse response = wolibalService.updateWork(memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/wolibals/rest")
    public ResponseEntity<ResponseDto> putRest(Authentication authentication, @RequestBody UpdateRestRequest request) {
        log.info("Request to PUT rest wolibal");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        UpdateWolibalResponse response = wolibalService.updateRest(memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/wolibals/sleep")
    public ResponseEntity<ResponseDto> putSleep(Authentication authentication, @RequestBody UpdateSleepRequest request) {
        log.info("Request to PUT sleep wolibal");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        UpdateWolibalResponse response = wolibalService.updateSleep(memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/wolibals/personal")
    public ResponseEntity<ResponseDto> putPersonal(Authentication authentication, @RequestBody UpdatePersonalRequest request) {
        log.info("Request to PUT persoanl wolibal");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        UpdateWolibalResponse response = wolibalService.updatePersonal(memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/wolibals/health")
    public ResponseEntity<ResponseDto> putHealth(Authentication authentication, @RequestBody UpdateHealthRequest request) {
        log.info("Request to PUT health wolibal");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        UpdateWolibalResponse response = wolibalService.updateHealth(memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 테스트 더미
    @PostMapping("/wolibals")
    public ResponseEntity<ResponseDto> postWolibalWithDate(Authentication authentication, @RequestBody CreateWolibalRequest request) {
        log.info("Request to POST wolibal with date");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        wolibalService.initializeWolibalWithDate(memberId, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/wolibals/work/{date}")
    public ResponseEntity<ResponseDto> putWorkWithDate(Authentication authentication, @PathVariable LocalDate date, @RequestBody UpdateWorkRequest request) {
        log.info("Request to PUT work wolibal with date");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        UpdateWolibalResponse response = wolibalService.updateWorkWithDate(memberId, date, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/wolibals/rest/{date}")
    public ResponseEntity<ResponseDto> putRestWithDate(Authentication authentication, @PathVariable LocalDate date, @RequestBody UpdateRestRequest request) {
        log.info("Request to PUT rest wolibal with date");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        UpdateWolibalResponse response = wolibalService.updateRestWithDate(memberId, date, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/wolibals/sleep/{date}")
    public ResponseEntity<ResponseDto> putSleepWithDate(Authentication authentication, @PathVariable LocalDate date, @RequestBody UpdateSleepRequest request) {
        log.info("Request to PUT sleep wolibal with date");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        UpdateWolibalResponse response = wolibalService.updateSleepWithDate(memberId, date, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/wolibals/personal/{date}")
    public ResponseEntity<ResponseDto> putPersonalWithDate(Authentication authentication, @PathVariable LocalDate date, @RequestBody UpdatePersonalRequest request) {
        log.info("Request to PUT persoanl wolibal with date");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        UpdateWolibalResponse response = wolibalService.updatePersonalWithDate(memberId, date, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/wolibals/health/{date}")
    public ResponseEntity<ResponseDto> putHealthWithDate(Authentication authentication, @PathVariable LocalDate date, @RequestBody UpdateHealthRequest request) {
        log.info("Request to PUT health wolibal with date");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        UpdateWolibalResponse response = wolibalService.updateHealthWithDate(memberId, date, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

