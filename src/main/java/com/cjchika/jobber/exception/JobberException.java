package com.cjchika.jobber.exception;

import org.springframework.http.HttpStatus;

public class JobberException extends RuntimeException{
    private final HttpStatus status;

    public JobberException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
