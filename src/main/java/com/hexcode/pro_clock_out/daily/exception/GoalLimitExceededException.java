package com.hexcode.pro_clock_out.daily.exception;

public class GoalLimitExceededException extends RuntimeException {

    public GoalLimitExceededException() {super("Goal은 최대 10개까지만 추가할 수 있습니다.");}
}
