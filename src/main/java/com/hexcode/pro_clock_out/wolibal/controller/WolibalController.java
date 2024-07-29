package com.hexcode.pro_clock_out.wolibal.controller;

import com.hexcode.pro_clock_out.auth.dto.CustomUserDetails;
import com.hexcode.pro_clock_out.global.dto.ResponseDto;
import com.hexcode.pro_clock_out.wolibal.dto.FindLabelsWolibalResponse;
import com.hexcode.pro_clock_out.wolibal.dto.FindTotalWolibalResponse;
import com.hexcode.pro_clock_out.wolibal.service.WolibalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WolibalController {
//    private final WolibalService wolibalService;

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
}
