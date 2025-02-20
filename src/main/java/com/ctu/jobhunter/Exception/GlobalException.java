package com.ctu.jobhunter.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ctu.jobhunter.dto.api.RestResponse;
import com.ctu.jobhunter.service.error.IdInvalidException;

@RestControllerAdvice
class GlobalException {
    @ExceptionHandler(value = IdInvalidException.class)
    public ResponseEntity<RestResponse<Object>> handleException(IdInvalidException invalidException){
        RestResponse<Object> res = RestResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("invalidException")
                .error(invalidException.getMessage())
                .build();
        return ResponseEntity.badRequest().body(res);
    }
    
}