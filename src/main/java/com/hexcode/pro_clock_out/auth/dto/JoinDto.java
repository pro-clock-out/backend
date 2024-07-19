package com.hexcode.pro_clock_out.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinDto {
    private String email;
    private String password;
}