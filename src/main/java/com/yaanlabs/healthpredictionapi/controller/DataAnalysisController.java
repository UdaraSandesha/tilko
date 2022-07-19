package com.yaanlabs.healthpredictionapi.controller;

import com.yaanlabs.healthpredictionapi.HealthPredictionAppException;
import com.yaanlabs.healthpredictionapi.model.*;
import com.yaanlabs.healthpredictionapi.service.HealthCheckupItemService;
import com.yaanlabs.healthpredictionapi.service.HealthCheckupService;
import com.yaanlabs.healthpredictionapi.service.UserService;
import com.yaanlabs.healthpredictionapi.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/api/analysis")
public class DataAnalysisController {

    @Autowired
    HealthCheckupService healthCheckupService;

    @Autowired
    UserService userService;

    @Autowired
    HealthCheckupItemService healthCheckupItemService;

    @Value("${application.prediction.api.uri}")
    private String predictionAPIURI;

    Map<String, String> healthCheckupShortCodeMap;

    @PostConstruct
    public void initialize() {
        healthCheckupShortCodeMap = new HashMap<>();
        List<HealthCheckupItem> healthCheckupItems = healthCheckupItemService.getAllCheckupItems();
        healthCheckupItems.forEach(healthCheckupItem ->
                healthCheckupShortCodeMap.put(healthCheckupItem.getCheckupItem(), healthCheckupItem.getShortName()));
    }

    @PostMapping("/abnormality/{user_id}/{count}")
    public ResponseEntity<?> getAbnormalityResult(@PathVariable("user_id") Integer userID, @PathVariable Integer count,
                                                  HttpServletRequest request) {
        CommonUtils.validateTokenAndDataRequestedUser(request, userID);
        try {
            URI requestURI = CommonUtils.addPathToURI(predictionAPIURI, "abnormality");
            List<HealthCheckupResult> latestCheckUps = healthCheckupService.getLatestCheckupsForUser(userID, count);
            if (latestCheckUps.isEmpty()) {
                return APIAppResponse.getResponseEntity("analysis.not.enough.data", HttpStatus.BAD_REQUEST);
            }

            List<String> predictionOutputs = new ArrayList<>();
            for (HealthCheckupResult checkupResult : latestCheckUps) {
                MultiValueMap<String, String> abnormalityInput = createAbnormalityInput(userID, checkupResult);
                ResponseEntity<String> response = getPredictionAPIResults(requestURI, abnormalityInput);
                if (response.getStatusCode() == HttpStatus.OK) {
                    predictionOutputs.add(response.getBody());
                } else {
                    return new ResponseEntity<>(response, response.getStatusCode());
                }
            }
            return new ResponseEntity<>(predictionOutputs, HttpStatus.OK);
        } catch (HealthPredictionAppException appException) {
            return appException.toResponseEntity();
        }
    }

    @PostMapping("/disease/{user_id}/{count}")
    public ResponseEntity<?> getDiseasePrediction(@PathVariable("user_id") Integer userID, @PathVariable Integer count,
                                                  HttpServletRequest request) {
        CommonUtils.validateTokenAndDataRequestedUser(request, userID);
        try {
            URI requestURI = CommonUtils.addPathToURI(predictionAPIURI, "disease_prediction");
            List<HealthCheckupResult> latestCheckUps = healthCheckupService.getLatestCheckupsForUser(userID, count);
            if (latestCheckUps.isEmpty()) {
                return APIAppResponse.getResponseEntity("analysis.not.enough.data", HttpStatus.BAD_REQUEST);
            }

            List<String> predictionOutputs = new ArrayList<>();
            for (HealthCheckupResult checkupResult : latestCheckUps) {
                MultiValueMap<String, String> diseasePredictionInput = createDiseasePredictionInput(userID, checkupResult);
                ResponseEntity<String> response = getPredictionAPIResults(requestURI, diseasePredictionInput);
                if (response.getStatusCode() == HttpStatus.OK) {
                    predictionOutputs.add(response.getBody());
                } else {
                    return new ResponseEntity<>(response, response.getStatusCode());
                }
            }
            return new ResponseEntity<>(predictionOutputs, HttpStatus.OK);
        } catch (HealthPredictionAppException appException) {
            return appException.toResponseEntity();
        }
    }

    private MultiValueMap<String, String> createAbnormalityInput(Integer userID, HealthCheckupResult healthCheckupResult) throws HealthPredictionAppException {
        return createAnalysisInput(userID, healthCheckupResult);
    }

    private MultiValueMap<String, String> createDiseasePredictionInput(Integer userID, HealthCheckupResult healthCheckupResult) throws HealthPredictionAppException {
        MultiValueMap<String, String> diseasePredictionInput = createAnalysisInput(userID, healthCheckupResult);
        diseasePredictionInput.add("right_vision", String.valueOf(1.0));
        diseasePredictionInput.add("left_vision", String.valueOf(1.0));
        return diseasePredictionInput;
    }

    private MultiValueMap<String, String> createAnalysisInput(Integer userID, HealthCheckupResult healthCheckupResult) throws HealthPredictionAppException {
        MultiValueMap<String, String> analysisInput = new LinkedMultiValueMap<>();
        addUserDetailsToAnalysisInput(analysisInput, userID);
        addHealthCheckupDetailsToAnalysisInput(analysisInput, healthCheckupResult);
        return analysisInput;
    }

    private void addUserDetailsToAnalysisInput(MultiValueMap<String, String> analysisInput, Integer userID) throws HealthPredictionAppException {
        User user = userService.findUserByID(userID);

        analysisInput.add("name", user.getName());
        analysisInput.add("sex", String.valueOf(CommonUtils.getIntGender(user.getGender())));
        analysisInput.add("age", CommonUtils.getAgeFromBirthday(user.getBirthday()).toString());
    }

    private void addHealthCheckupDetailsToAnalysisInput(MultiValueMap<String, String> analysisInput, HealthCheckupResult healthCheckupResult) {
        Set<HealthCheckupValue> healthCheckupValues = healthCheckupResult.getCheckupValues();
        for (HealthCheckupValue healthCheckupValue : healthCheckupValues) {
            String shortCode = healthCheckupShortCodeMap.get(healthCheckupValue.getCheckupItem());
            analysisInput.add(shortCode, String.valueOf(healthCheckupValue.getValue()));
        }
    }

    private ResponseEntity<String> getPredictionAPIResults(URI requestURI, MultiValueMap<String, String> predictionInput) {
        HttpHeaders headers = CommonUtils.createURLEncodedContentTypeHeader();
        return CommonUtils.makeRESTCall(requestURI, HttpMethod.POST, headers, predictionInput, String.class);
    }
}
