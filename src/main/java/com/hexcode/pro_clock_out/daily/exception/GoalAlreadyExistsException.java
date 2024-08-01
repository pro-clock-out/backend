package com.hexcode.pro_clock_out.daily.exception;

public class GoalAlreadyExistsException extends RuntimeException {
    public GoalAlreadyExistsException(String name) {super("이미 존재하는 Goal입니다.");}
}
