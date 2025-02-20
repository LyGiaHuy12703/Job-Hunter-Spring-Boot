package com.ctu.jobhunter.Exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ctu.jobhunter.service.error.IdInvalidException;

@RestControllerAdvice
class GlobalException {
    @ExceptionHandler(value = IdInvalidException.class)
    public ResponseEntity<String> handleException(IdInvalidException invalidException){
        return ResponseEntity.badRequest().body("Id Invalid");
    }
    
}