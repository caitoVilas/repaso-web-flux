package com.caito.tutorialwebflux.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter @Setter
public class CustomException extends Exception{
    private HttpStatus status;

    public CustomException(HttpStatus status, String msg){
        super(msg);
        this.status = status;
    }
}
