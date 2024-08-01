package com.hexcode.pro_clock_out.auth.exception;

public class TokenException extends RuntimeException{
    public TokenException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
