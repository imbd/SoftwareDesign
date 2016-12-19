package com.imbd.spbau;

/**
 * Exception that shows syntax errors in command line and in errors in command execution
 */

public class SyntaxException extends RuntimeException {

    private String message;

    public SyntaxException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
