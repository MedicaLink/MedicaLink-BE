package com.medicalink.MedicaLink_backend.utils.HttpExceptions;

import org.springframework.http.HttpStatus;

public class HttpException extends RuntimeException{
    private String message;
    private HttpStatus status;

    public HttpException(String message) {
        super(message);
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
