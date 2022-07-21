package com.yaanlabs.healthpredictionapi.controller;

import com.nimbusds.jose.shaded.json.parser.ParseException;
import com.yaanlabs.healthpredictionapi.dto.HealthCheckResponse;
import com.yaanlabs.healthpredictionapi.dto.SimpleAuthRequest;
import com.yaanlabs.healthpredictionapi.service.HealthService;
import com.yaanlabs.healthpredictionapi.service.SimpleAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping("simpleAuth")
public class SimpleAuthController {

    @Autowired
    SimpleAuthenticationService simpleAuthenticationService;

    @Autowired
    HealthService healthService;

    @GetMapping("test")
    public ResponseEntity<Object> testSimpleAuth() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, IOException, ParseException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {

        SimpleAuthRequest simpleAuthRequest = new SimpleAuthRequest();
        simpleAuthRequest.setPrivateAuthType("0");
        simpleAuthRequest.setUsername("강성봉");
        simpleAuthRequest.setBirthDate("19880512");
        simpleAuthRequest.setUserCellPhoneNumber("01080040520");

        return healthService.getHealthData(simpleAuthRequest);
    }
}
