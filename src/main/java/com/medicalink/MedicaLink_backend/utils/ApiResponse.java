package com.medicalink.MedicaLink_backend.utils;

public class ApiResponse<T,E> {
    private T data;
    private E errors;

    public ApiResponse(T data, E errors) {
        this.data = data;
        this.errors = errors;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public E getErrors() {
        return errors;
    }

    public void setErrors(E errors) {
        this.errors = errors;
    }
}
