package com.sofram.shared.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException exception, HttpServletRequest request) {
        List<FieldErrorDetail> fieldErrors = exception.getBindingResult().getFieldErrors().stream()
                .map(this::toFieldErrorDetail)
                .toList();
        return build(HttpStatus.BAD_REQUEST, "Solicitud invalida", request, fieldErrors);
    }

    @ExceptionHandler(BadCredentialsException.class)
    ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException exception, HttpServletRequest request) {
        return build(HttpStatus.UNAUTHORIZED, "Credenciales invalidas", request, List.of());
    }

    @ExceptionHandler(DisabledException.class)
    ResponseEntity<ApiError> handleDisabled(DisabledException exception, HttpServletRequest request) {
        return build(HttpStatus.FORBIDDEN, "Usuario inactivo", request, List.of());
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiError> handleGeneric(Exception exception, HttpServletRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno", request, List.of());
    }

    private FieldErrorDetail toFieldErrorDetail(FieldError fieldError) {
        return new FieldErrorDetail(fieldError.getField(), fieldError.getDefaultMessage());
    }

    private ResponseEntity<ApiError> build(
            HttpStatus status,
            String message,
            HttpServletRequest request,
            List<FieldErrorDetail> fieldErrors
    ) {
        ApiError apiError = new ApiError(
                Instant.now().toString(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                fieldErrors
        );
        return ResponseEntity.status(status).body(apiError);
    }
}
