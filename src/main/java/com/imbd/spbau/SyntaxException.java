package com.imbd.spbau;

public class SyntaxException extends RuntimeException {

    private String message;

    public SyntaxException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
