package com.hexcode.pro_clock_out.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@ResponseBody
@RequestMapping("/api/v1")
public class MainController {

    @GetMapping("/")
    public String mainP() {

        return "main Controller";
    }
}
