package com.hexcode.pro_clock_out.daily.controller;

import com.hexcode.pro_clock_out.daily.domain.Daily;
import com.hexcode.pro_clock_out.daily.dto.*;
import com.hexcode.pro_clock_out.daily.service.DailyService;
import com.hexcode.pro_clock_out.global.dto.ResponseDto;
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
public class DailyController {

    private final DailyService dailyService;

    // 발자국 상세 조회
    @GetMapping("/daily/{dailyId}")
    public ResponseEntity<ResponseDto> getDailyFootprintDetails(Authentication authentication, @PathVariable("dailyId") Long dailyId) {
        log.info("Request to get Daily Footprint Details");
        Long memberId = (Long) authentication.getPrincipal();
        FindDailyDetailResponse response = dailyService.findDailyDetail(dailyId, memberId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    // 연간 발자국 조회
    @GetMapping("/daily")
    public ResponseEntity<ResponseDto> getTotalDailyFootprints(Authentication authentication, @RequestParam("year") int year) {
        log.info("Request to get Total Daily Footprints for year {}", year);
        Long memberId = (Long) authentication.getPrincipal();
        FindTotalDailyResponse response = dailyService.findTotalDaily(memberId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 발자국 추가
    @PostMapping("/daily/{dailyId}")
    public ResponseEntity<ResponseDto> addDailyFootprint(Authentication authentication, @PathVariable("dailyId") Long dailyId, @RequestBody UpdateDailyRequest request) {
        log.info("Request to post Footprint");
        Long memberId = (Long) authentication.getPrincipal();
        UpdateDailyResponse response = dailyService.addDaily(dailyId, memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 발자국 수정
    @PutMapping("/daily/{dailyId}")
    public ResponseEntity<ResponseDto> updateDailyFootprint(Authentication authentication, @PathVariable("dailyId") Long dailyId, @RequestBody UpdateDailyRequest request) {
        log.info("Request to put Footprint");
        Long memberId = (Long) authentication.getPrincipal();
        UpdateDailyResponse response = dailyService.updateDaily(dailyId, memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 목표 활동 조회
    @GetMapping("/daily/{dailyId}/goals")
    public ResponseEntity<ResponseDto> getGoals(Authentication authentication, @PathVariable("dailyId") Long dailyId) {
        log.info("Request to get Goals");
        Long memberId = (Long) authentication.getPrincipal();
        FindGoalResponse response = dailyService.findGoals(dailyId, memberId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 목표 활동 추가
//    @PostMapping("/daily/{dailyId}/goals")
//    public ResponseEntity<ResponseDto> addGoals(Authentication authentication, @PathVariable("dailyId") Long dailyId, @RequestBody UpdateGoalRequest request) {
//        log.info("Request to post goals");
//        Long memberId = (Long) authentication.getPrincipal();
//        UpdateGoalResponse response = dailyService.addGoals(dailyId, memberId, request);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }



    // 목표 활동 삭제
//    @DeleteMapping("/daily/{dailyId}/goals/{goalId}")
//    public ResponseEntity<ResponseDto> deleteGoals(Authentication authentication, @PathVariable("dailyId") Long dailyId, @PathVariable("goalId") Long goalId,)  {
//        log.info("Request to delete goals");
//        Long memberId = (Long) authentication.getPrincipal();
//        DeleteGoalResponse response = dailyService.deleteGoals(dailyId, goalId, memberId);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
}
