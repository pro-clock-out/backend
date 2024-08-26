package com.hexcode.pro_clock_out.wolibal.exception;

import com.hexcode.pro_clock_out.wolibal.domain.Health;
import com.hexcode.pro_clock_out.wolibal.domain.Wolibal;

public class HealthNotFoundException extends RuntimeException {

    public HealthNotFoundException() { super("Health를 찾을 수 없습니다.");}

    public HealthNotFoundException(Long healthId) {
        super("Health: " + healthId + "를 찾을 수 없습니다.");
    }

    public HealthNotFoundException(Wolibal wolibal) {
        super("Wolibal: " + wolibal.getId() + "의 Health를 찾을 수 없습니다.");
    }
}
