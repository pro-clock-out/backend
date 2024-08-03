package com.hexcode.pro_clock_out.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class JoinDto {
    private String email;
    private String password;
}