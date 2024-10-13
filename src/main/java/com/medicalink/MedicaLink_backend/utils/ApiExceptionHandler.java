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
        return buildResponseEntity(new ApiError(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NotFoundException.class, UnAuthorizedException.class})
    private ResponseEntity<Object> handleNotFoundException(HttpException exception) {
        return buildResponseEntity(new ApiError(exception.getMessage()), exception.getStatus());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    private ResponseEntity<Object> handleTokenExpiration(ExpiredJwtException exception) {
        return buildResponseEntity(new ApiError(exception.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<Object> handleOther(Exception exception) {
        exception.printStackTrace();
        return buildResponseEntity(new ApiError(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError, HttpStatus status) {
        var response = new ApiResponse<Object,ApiError>(null, apiError);
        return new ResponseEntity<>(response, status);
    }
}
