package com.itberries.technopark.itberries.responses;


public class AwesomeException {
    private String message;

    public AwesomeException() {
    }

    public AwesomeException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
