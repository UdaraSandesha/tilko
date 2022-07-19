package com.yaanlabs.healthpredictionapi;

import com.yaanlabs.healthpredictionapi.model.APIAppResponse;
import com.yaanlabs.healthpredictionapi.service.Translator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HealthPredictionAppException extends Throwable {

    private String messageCode;
    private Object[] messageArgs;

    public HealthPredictionAppException(String messageCode, Object... args) {
        super(String.format(Translator.toLocale(messageCode), args));
        this.messageCode = messageCode;
        this.messageArgs = args;
    }

    public ResponseEntity<?> toResponseEntity() {
        return new ResponseEntity<>(new APIAppResponse(this.messageCode, this.messageArgs), HttpStatus.BAD_REQUEST);
    }
}
