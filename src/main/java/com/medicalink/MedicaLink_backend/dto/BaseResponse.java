package com.medicalink.MedicaLink_backend.dto;

public class BaseResponse {
    private String message;
    private boolean success;

    public BaseResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public BaseResponse() {
    }

    public String getMessage() {
        return message;
    }

    public BaseResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public BaseResponse setSuccess(boolean success) {
        this.success = success;
        return this;
    }
}
