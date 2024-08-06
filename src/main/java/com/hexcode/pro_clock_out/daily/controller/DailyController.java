package com.hexcode.pro_clock_out.daily.controller;

import com.hexcode.pro_clock_out.auth.dto.CustomUserDetails;
import com.hexcode.pro_clock_out.daily.dto.*;
import com.hexcode.pro_clock_out.daily.service.DailyService;
import com.hexcode.pro_clock_out.global.dto.ResponseDto;
import com.hexcode.pro_clock_out.global.service.S3Service;
import io.jsonwebtoken.MalformedJwtException;
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
public class DailyController {
    private final DailyService dailyService;
    private final S3Service s3Service;

    // 연간 발자국 조회
    @GetMapping("/daily")
    public ResponseEntity<ResponseDto> getTotalDailyFootprints(Authentication authentication, @RequestParam("year") int year) {
        log.info("Request to get Total Daily Footprints for year {}", year);
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        FindTotalDailyResponse response = dailyService.findTotalDaily(memberId, year);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 발자국 상세 조회
    @GetMapping("/daily/{dailyId}")
    public ResponseEntity<ResponseDto> getDailyFootprintDetails(Authentication authentication, @PathVariable("dailyId") Long dailyId) {
        log.info("Request to get Daily Footprint Details");
        try {
            Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
            FindDailyDetailResponse response = dailyService.findDailyDetail(dailyId, memberId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (MalformedJwtException e) {
            log.error("Invalid JWT format: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 발자국 이미지 업로드
    @PostMapping("/daily/image")
    public String addDailyFootprintImage(Authentication authentication, @RequestParam MultipartFile file) {
        log.info("Request to post Footprint image");
        return s3Service.uploadFile(file);
    }

    // 발자국 추가
    @PostMapping("/daily")
    public ResponseEntity<ResponseDto> addDailyFootprint(Authentication authentication, @RequestBody CreateDailyRequest request) {
        log.info("Request to post Footprint");
        try {
            Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
            CreateDailyResponse response = dailyService.addDaily(memberId, request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (MalformedJwtException e) {
            log.error("Invalid JWT format: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 발자국 수정
    @PutMapping("/daily/{dailyId}")
    public ResponseEntity<ResponseDto> updateDailyFootprint(Authentication authentication, @PathVariable("dailyId") Long dailyId, @RequestBody UpdateDailyRequest request) {
        log.info("Request to put Footprint");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        UpdateDailyResponse response = dailyService.updateDaily(dailyId, memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 목표 활동 조회
    @GetMapping("/daily/goals")
    public ResponseEntity<ResponseDto> getGoals(Authentication authentication) {
        log.info("Request to get Goals");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        FindGoalResponse response = dailyService.findGoals(memberId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 목표 활동 추가
    @PostMapping("/daily/goals")
    public ResponseEntity<ResponseDto> addGoals(Authentication authentication, @RequestBody CreateGoalRequest request) {
        log.info("Request to post goals");
        try {
            Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
            CreateGoalResponse response = dailyService.addGoals(memberId, request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (MalformedJwtException e) {
            log.error("Invalid JWT format: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 목표 활동 수정
    @PutMapping("daily/goals/{goalId}")
    public  ResponseEntity<ResponseDto> updateGoals(Authentication authentication, @PathVariable("goalId") Long goalId, @RequestBody UpdateGoalRequest request) {
        log.info("Request to put goals");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        UpdateGoalResponse response = dailyService.updateGoals(goalId, memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 목표 활동 삭제
    @DeleteMapping("/daily/goals/{goalId}")
    public ResponseEntity<ResponseDto> deleteGoals(Authentication authentication,  @PathVariable("goalId") Long goalId)  {
        log.info("Request to delete goals");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        DeleteGoalResponse response = dailyService.deleteGoals(goalId, memberId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
