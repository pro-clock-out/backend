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

//    @GetMapping("/wolibals/label")
//    public ResponseEntity<ResponseDto> getLabelsWolibal(Authentication authentication, @RequestParam("option") String option) {
//        log.info("Request to get labels wolibal");
//        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
//        FindLabelsWolibalResponse response = wolibalService.findLabelsWolibal(memberId, option);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

    @GetMapping("/wolibals/work/{workId}")
    public ResponseEntity<ResponseDto> getWorkWolibal(Authentication authentication, @PathVariable("workId") Long workId) {
        log.info("Request to GET work wolibal");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        FindScoreRankAvgResponse response = wolibalService.findWork(memberId, workId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/wolibals/rest/{restId}")
    public ResponseEntity<ResponseDto> getRestWolibal(Authentication authentication, @PathVariable("restId") Long restId) {
        log.info("Request to GET rest wolibal");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        FindScoreRankAvgResponse response = wolibalService.findRest(memberId, restId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/wolibals/sleep/{sleepId}")
    public ResponseEntity<ResponseDto> getSleepWolibal(Authentication authentication, @PathVariable("sleepId") Long sleepId) {
        log.info("Request to GET sleep wolibal");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        FindScoreRankAvgResponse response = wolibalService.findSleep(memberId, sleepId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/wolibals/personal/{personalId}")
    public ResponseEntity<ResponseDto> getPersonalWolibal(Authentication authentication, @PathVariable("personalId") Long personalId) {
        log.info("Request to GET personal wolibal");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        FindScoreRankAvgResponse response = wolibalService.findPersonal(memberId, personalId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/wolibals/health/{healthId}")
    public ResponseEntity<ResponseDto> getHealthWolibal(Authentication authentication, @PathVariable("healthId") Long healthId) {
        log.info("Request to GET health wolibal");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        FindScoreRankAvgResponse response = wolibalService.findHealth(memberId, healthId);
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

    @PostMapping("/wolibals/work")
    public ResponseEntity<ResponseDto> postWork(Authentication authentication, @RequestBody CreateWorkRequest request) {
        log.info("Request to POST work wolibal");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        CreateWolibalResponse response = wolibalService.createWork(memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/wolibals/rest")
    public ResponseEntity<ResponseDto> postRest(Authentication authentication, @RequestBody CreateRestRequest request) {
        log.info("Request to POST rest wolibal");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        CreateWolibalResponse response = wolibalService.createRest(memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/wolibals/sleep")
    public ResponseEntity<ResponseDto> postSleep(Authentication authentication, @RequestBody CreateSleepRequest request) {
        log.info("Request to POST sleep wolibal");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        CreateWolibalResponse response = wolibalService.createSleep(memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/wolibals/personal")
    public ResponseEntity<ResponseDto> postPersonal(Authentication authentication, @RequestBody CreatePersonalRequest request) {
        log.info("Request to POST personal wolibal");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        CreateWolibalResponse response = wolibalService.createPersonal(memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/wolibals/health")
    public ResponseEntity<ResponseDto> postHealth(Authentication authentication, @RequestBody CreateHealthRequest request) {
        log.info("Request to POST heath wolibal");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        CreateWolibalResponse response = wolibalService.createHealth(memberId, request);
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

