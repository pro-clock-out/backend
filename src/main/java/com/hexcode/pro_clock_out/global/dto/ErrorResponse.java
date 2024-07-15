package com.hexcode.pro_clock_out.global.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class ErrorResponse {
    private Map<String, String> errorMessages;
}
