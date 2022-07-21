package com.yaanlabs.healthpredictionapi.service;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.shaded.json.parser.ParseException;
import com.yaanlabs.healthpredictionapi.dto.HealthCheckResponse;
import com.yaanlabs.healthpredictionapi.dto.SimpleAuthRequest;
import com.yaanlabs.healthpredictionapi.dto.SimpleAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
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
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class HealthService {
    
    @Autowired
    SimpleAuthentication simpleAuthentication;    
    @Autowired
    SimpleAuthenticationService simpleAuthenticationService;
    Logger logger = LoggerFactory.getLogger(HealthService.class);

    public static final MediaType APPLICATION_JSON = new MediaType("application", "json");
    private Random random = SecureRandom.getInstanceStrong();



    public HealthService() throws NoSuchAlgorithmException {
    }

    /**
     *
     * @return Latest health record as a HealthCheckResponse
     * description authenticates with simple auth and gets data from health check API
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws IOException
     * @throws ParseException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     */
    public ResponseEntity<Object> getHealthData(SimpleAuthRequest simpleAuthRequest) throws InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, IOException, ParseException, NoSuchAlgorithmException, InvalidKeySpecException,
            BadPaddingException, InvalidKeyException {

        SimpleAuthResponse simpleAuthResponse = new SimpleAuthResponse();

        CompletableFuture<SimpleAuthResponse> simpleAuth = simpleAuthenticationService.simpleAuth(simpleAuthRequest);

        try{
            simpleAuthResponse = simpleAuth.get();
            logger.info(String.format("Waiting %d minute(s) until the confirmation is received ", 1));
            Thread.sleep(3600000); // thread sleep for a minute
        } catch(ExecutionException | InterruptedException ex) {
            logger.info("Interrupted!", ex);
            Thread.currentThread().interrupt();
        }

        if(simpleAuthResponse != null) {

            String rsaPublicKey = simpleAuthentication.getPublicKey();
            logger.info("rsaPublicKey: " + rsaPublicKey);

            byte[] aesKey = new byte[16];
            random.nextBytes(aesKey);

            byte[] aesIv = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

            String aesCipherKey = simpleAuthentication.rsaEncrypt(rsaPublicKey, aesKey);
            logger.info("aesCipherKey: " + aesCipherKey);

            String url = simpleAuthentication.apiHost + "api/v1.0/NhisSimpleAuth/Ggpab003M0105";

            JSONObject reqData = new JSONObject();
            reqData.put("CxId", simpleAuthResponse.getCxId());
            reqData.put("PrivateAuthType", simpleAuthResponse.getPrivateAuthType());
            reqData.put("ReqTxId", simpleAuthResponse.getReqTxId());
            reqData.put("Token", simpleAuthResponse.getToken());
            reqData.put("TxId", simpleAuthResponse.getTxId());
            reqData.put("UserName", simpleAuthResponse.getUsername());
            reqData.put("BirthDate", simpleAuthResponse.getBirthDate());
            reqData.put("UserCellphoneNumber", simpleAuthResponse.getUserCellPhoneNumber());

            JSONObject json = new JSONObject();

            json.put("CxId", reqData.get("CxId"));
            json.put("PrivateAuthType", reqData.get("PrivateAuthType"));
            json.put("ReqTxId", reqData.get("ReqTxId"));
            json.put("Token", reqData.get("Token"));
            json.put("TxId", reqData.get("TxId"));
            json.put("UserName", simpleAuthentication.aesEncrypt(aesKey, aesIv, (String) reqData.get("UserName")));
            json.put("BirthDate", simpleAuthentication.aesEncrypt(aesKey, aesIv, (String) reqData.get("BirthDate")));
            json.put("UserCellphoneNumber", simpleAuthentication.aesEncrypt(aesKey, aesIv, (String) reqData.get("UserCellphoneNumber")));
            json.put("기타필요한파라미터", simpleAuthentication.aesEncrypt(aesKey, aesIv, (String) reqData.get("UserName")));

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("API-KEY", simpleAuthentication.apiKey);
            headers.add("ENC-KEY", aesCipherKey);

            HashMap<String, Object> response;

            try {
                HttpEntity<String> entity = new HttpEntity<>(json.toJSONString(), headers);
                response =  (HashMap<String, Object>) restTemplate.postForObject(url, entity, HashMap.class);
            } catch(Exception e) {
                logger.info("API call unsuccessful");
                return ResponseEntity.ok("API call unsuccessful");
            }
            if(response != null) {
                String status = (String) response.get("Status");
                if (status.equals("OK")) {
                    return ResponseEntity.ok(response.get("ResultsSet"));
                } else {
                    return ResponseEntity.ok("User not confirmed yet");
                }
            }
        }
        return ResponseEntity.ok("User not confirmed yet");
    }
}