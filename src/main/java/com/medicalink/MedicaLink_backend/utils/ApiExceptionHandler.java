package com.medicalink.MedicaLink_backend.utils;

import com.medicalink.MedicaLink_backend.utils.HttpExceptions.HttpException;
import com.medicalink.MedicaLink_backend.utils.HttpExceptions.NotFoundException;
import com.medicalink.MedicaLink_backend.utils.HttpExceptions.UnAuthorizedException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler({NotFoundException.class, UnAuthorizedException.class})
    private ResponseEntity<Object> handleNotFoundException(HttpException exception) {
        return buildResponseEntity(new ApiError(exception.getStatus(), exception.getMessage()));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    private ResponseEntity<Object> handleTokenExpiration(ExpiredJwtException exception) {
        return buildResponseEntity(new ApiError(HttpStatus.UNAUTHORIZED, exception.getMessage()));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
