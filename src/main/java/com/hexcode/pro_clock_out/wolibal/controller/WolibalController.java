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
    @PutMapping("/wolibals/work/{workId}")
    public ResponseEntity<ResponseDto> updateWork(Authentication authentication, @PathVariable("workId") Long workId, @RequestBody UpdateWorkRequest request) {
        log.info("Request to PUT work wolibal");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        UpdateWolibalResponse response = wolibalService.updateWork(workId, memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/wolibals/rest/{restId}")
    public ResponseEntity<ResponseDto> updateRest(Authentication authentication, @PathVariable("restId") Long workId, @RequestBody UpdateRestRequest request) {
        log.info("Request to PUT rest wolibal");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        UpdateWolibalResponse response = wolibalService.updateRest(workId, memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/wolibals/sleep/{sleepId}")
    public ResponseEntity<ResponseDto> updateSleep(Authentication authentication, @PathVariable("sleepId") Long sleepId, @RequestBody UpdateSleepRequest request) {
        log.info("Request to PUT sleep wolibal");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        UpdateWolibalResponse response = wolibalService.updateSleep(sleepId, memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/wolibals/personal/{personalId}")
    public ResponseEntity<ResponseDto> updatePersoanl(Authentication authentication, @PathVariable("personalId") Long personalId, @RequestBody UpdatePersonalRequest request) {
        log.info("Request to PUT persoanl wolibal");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        UpdateWolibalResponse response = wolibalService.updatePersonal(personalId, memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/wolibals/health/{healthId}")
    public ResponseEntity<ResponseDto> updatehealth(Authentication authentication, @PathVariable("healthId") Long healthId, @RequestBody UpdateHealthRequest request) {
        log.info("Request to PUT health wolibal");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        UpdateWolibalResponse response = wolibalService.updateHealth(healthId, memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

