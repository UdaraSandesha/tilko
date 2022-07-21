package com.yaanlabs.healthpredictionapi.service;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.shaded.json.parser.ParseException;
import com.yaanlabs.healthpredictionapi.dto.SimpleAuthRequest;
import com.yaanlabs.healthpredictionapi.dto.SimpleAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
public class SimpleAuthenticationService {
    
    @Autowired
    SimpleAuthentication simpleAuthentication;
    
    Logger logger = LoggerFactory.getLogger(SimpleAuthenticationService.class);

    private Random random = SecureRandom.getInstanceStrong();

    public SimpleAuthenticationService() throws NoSuchAlgorithmException {
    }

    @Async("SimpleAuthExecutor")
    public CompletableFuture<SimpleAuthResponse> simpleAuth(SimpleAuthRequest simpleAuthRequest) throws IOException, ParseException, NoSuchPaddingException, IllegalBlockSizeException,
            NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException {
        
        String rsaPublicKey = simpleAuthentication.getPublicKey();
        logger.info("rsaPublicKey: " + rsaPublicKey);

        byte[] aesKey = new byte[16];
        random.nextBytes(aesKey);

        byte[] aesIv = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

        String aesCipherKey = simpleAuthentication.rsaEncrypt(rsaPublicKey, aesKey);
        logger.info("aesCipherKey: " + aesCipherKey);

        String url = simpleAuthentication.apiHost + "api/v1.0/NhisSimpleAuth/SimpleAuthRequest";

        JSONObject json = new JSONObject();
        json.put("PrivateAuthType", simpleAuthRequest.getPrivateAuthType());
        json.put("UserName", simpleAuthentication.aesEncrypt(aesKey, aesIv, simpleAuthRequest.getUsername()));
        json.put("BirthDate", simpleAuthentication.aesEncrypt(aesKey, aesIv, simpleAuthRequest.getBirthDate()));
        json.put("UserCellphoneNumber", simpleAuthentication.aesEncrypt(aesKey, aesIv, simpleAuthRequest.getUserCellPhoneNumber()));

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("API-KEY", simpleAuthentication.apiKey);
        headers.add("ENC-KEY", aesCipherKey);

        HashMap<String, Object> response = new HashMap<>();

        try {
            HttpEntity<String> entity = new HttpEntity<>(json.toJSONString(), headers);
            response =  (HashMap<String, Object>) restTemplate.postForObject(url, entity, HashMap.class);
        } catch(Exception e) {
            logger.info("API call unsuccessful");
        }

        if(response != null) {
            String status = (String) response.get("Status");

            if(status.equals("OK")) {
                HashMap<String, String> results = (HashMap<String, String>) response.get("ResultData");

                SimpleAuthResponse simpleAuthResponse = new SimpleAuthResponse();
                simpleAuthResponse.setToken(results.get("Token"));
                simpleAuthResponse.setTxId(results.get("TxId"));
                simpleAuthResponse.setCxId(results.get("CxId"));
                simpleAuthResponse.setReqTxId(results.get("ReqTxId"));
                simpleAuthResponse.setPrivateAuthType(results.get("PrivateAuthType"));
                simpleAuthResponse.setUsername(results.get("UserName"));
                simpleAuthResponse.setBirthDate(results.get("BirthDate"));
                simpleAuthResponse.setUserCellPhoneNumber(results.get("UserCellphoneNumber"));

                return CompletableFuture.completedFuture(simpleAuthResponse);
            }
        }
        return CompletableFuture.completedFuture(null);
    }
}
