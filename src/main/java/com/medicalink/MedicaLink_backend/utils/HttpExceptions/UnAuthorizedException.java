package com.medicalink.MedicaLink_backend.utils.HttpExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Represents an Authorized response
 * Status Code
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnAuthorizedException extends HttpException {

    public UnAuthorizedException(String message) {
        super(message);
        this.setStatus(HttpStatus.UNAUTHORIZED);
    }
}
