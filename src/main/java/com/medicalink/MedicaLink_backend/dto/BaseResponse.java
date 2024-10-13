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

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
