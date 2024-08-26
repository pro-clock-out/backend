package com.hexcode.pro_clock_out.wolibal.exception;

import com.hexcode.pro_clock_out.wolibal.domain.Wolibal;

public class WorkNotFoundException extends RuntimeException {

    public WorkNotFoundException() { super("Work를 찾을 수 없습니다.");}

    public WorkNotFoundException(Long workId) {
        super("Work: " + workId + "를 찾을 수 없습니다.");
    }

    public WorkNotFoundException(Wolibal wolibal) {
        super("Wolibal: " + wolibal.getId() + "의 Work를 찾을 수 없습니다.");
    }
}

