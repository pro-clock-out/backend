package com.hexcode.pro_clock_out.auth.controller;

import com.hexcode.pro_clock_out.auth.dto.JoinDto;
import com.hexcode.pro_clock_out.auth.service.JoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class JoinController {
    private final JoinService joinService;

    @GetMapping("/")
    public String hello() {
        log.info("Hello World~~~!!!");
        return "Hello";
    }

    @PostMapping("/signup")
    public String joinProcess(JoinDto joinDto) {
        log.info(joinDto.getEmail());
        joinService.joinProcess(joinDto);
        return joinDto.getEmail();
    }
}
