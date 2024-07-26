package com.hexcode.pro_clock_out.daily.exception;

public class GoalNotFoundException extends RuntimeException{

    public GoalNotFoundException() {
        super("Goal을 찾을 수 없습니다.");
    }

    public GoalNotFoundException(Long goalId) {
        super("Goal: " + goalId + "를 찾을 수 없습니다.");
    }
}

