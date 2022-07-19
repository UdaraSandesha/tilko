package com.yaanlabs.healthpredictionapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yaanlabs.healthpredictionapi.service.Translator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIAppResponse implements Serializable {

    private String message;

    public APIAppResponse(String messageCode, Object... args) {
        this.message = String.format(Translator.toLocale(messageCode), args);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ResponseEntity<?> getResponseEntity(String messageCode, HttpStatus httpStatus, Object... args) {
        return new ResponseEntity<>(new APIAppResponse(messageCode, args), httpStatus);
    }

}
