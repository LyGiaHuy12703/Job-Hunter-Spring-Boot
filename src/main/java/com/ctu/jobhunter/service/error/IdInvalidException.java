package com.ctu.jobhunter.service.error;

public class IdInvalidException extends RuntimeException {
    public IdInvalidException (String message){
        super(message);
    }
    
}
