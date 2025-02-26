package com.ctu.jobhunter.Exception;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.ctu.jobhunter.Exception.error.IdInvalidException;
import com.ctu.jobhunter.dto.api.RestResponse;

@RestControllerAdvice
class GlobalException {
    @ExceptionHandler(value = {
            UsernameNotFoundException.class,
            BadCredentialsException.class,
            IdInvalidException.class,
            HttpRequestMethodNotSupportedException.class
    })
    public ResponseEntity<RestResponse<Object>> handleException(Exception ex) {
        RestResponse<Object> res = RestResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("exception occurs")
                .error(ex.getMessage())
                .build();
        return ResponseEntity.badRequest().body(res);
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<RestResponse<Object>> handleExceptionNotFound(Exception ex) {
        RestResponse<Object> res = RestResponse.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message("404 - Not found. URL may not exist...")
                .error(ex.getMessage())
                .build();
        return ResponseEntity.badRequest().body(res);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> validationError(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();

        final List<FieldError> fieldErrors = result.getFieldErrors();

        RestResponse<Object> res = RestResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .error(exception.getBody().getDetail())
                .build();

        List<String> errors = fieldErrors.stream().map(f -> f.getDefaultMessage()).collect(Collectors.toList());
        res.setMessage(errors.size() > 1 ? errors : errors.get(0));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

}