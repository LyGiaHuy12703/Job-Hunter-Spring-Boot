package com.ctu.jobhunter.dto.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestResponse<T> {
    private int statusCode;
    private String error;

    //message có thể là string hay array list
    private Object message;
    private T data;

    
}
