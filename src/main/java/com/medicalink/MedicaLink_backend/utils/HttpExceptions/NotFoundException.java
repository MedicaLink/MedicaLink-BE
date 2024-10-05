package com.medicalink.MedicaLink_backend.utils.HttpExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Represents a not found response
 * Status code 404
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends HttpException {

    public NotFoundException(String message) {
        super(message);
        this.setStatus(HttpStatus.NOT_FOUND);
    }
}
