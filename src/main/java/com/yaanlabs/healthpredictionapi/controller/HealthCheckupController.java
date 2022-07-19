package com.yaanlabs.healthpredictionapi.controller;

import com.yaanlabs.healthpredictionapi.HealthPredictionAppException;
import com.yaanlabs.healthpredictionapi.model.APIAppResponse;
import com.yaanlabs.healthpredictionapi.model.HealthCheckupResult;
import com.yaanlabs.healthpredictionapi.model.HealthCheckupValue;
import com.yaanlabs.healthpredictionapi.model.User;
import com.yaanlabs.healthpredictionapi.service.HealthCheckupService;
import com.yaanlabs.healthpredictionapi.service.UserService;
import com.yaanlabs.healthpredictionapi.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/checkup")
public class HealthCheckupController {

    private static final String HEALTH_CHECKUP_ITEM_HEIGHT = "키";
    private static final String HEALTH_CHECKUP_ITEM_WEIGHT = "몸무게";
    private static final String UNIQUE_CONSTRAINT_USER_CHECKUP_DATE = "'health_checkup_result.user_health_checkup_date_unique'";

    @Autowired
    HealthCheckupService healthCheckupService;

    @Autowired
    UserService userService;

    @PostMapping("/add")
    public ResponseEntity<?> addHealthCheckup(@RequestBody HealthCheckupResult healthCheckupResult, HttpServletRequest request) {
        CommonUtils.validateTokenAndDataRequestedUser(request, healthCheckupResult.getUserID());
        try {
            for (HealthCheckupValue healthCheckupValue : healthCheckupResult.getCheckupValues()) {
                healthCheckupValue.setHealthCheckupResult(healthCheckupResult);
            }
            healthCheckupService.addHealthCheckupResult(healthCheckupResult);
            updateHeightWeightInUserProfile(healthCheckupResult.getUserID());
            return APIAppResponse.getResponseEntity("checkup.add.success", HttpStatus.OK);
        } catch (DataIntegrityViolationException exception) {
            if (CommonUtils.isUniqueKeyIntegrityViolation(exception, UNIQUE_CONSTRAINT_USER_CHECKUP_DATE)) {
                return APIAppResponse.getResponseEntity("checkup.add.already.available", HttpStatus.BAD_REQUEST);
            }
            throw exception;
        }
    }
    @GetMapping("/latest/{user_id}/{count}")
    public ResponseEntity<?> getLatestCheckupForUser(@PathVariable("user_id") Integer userID, @PathVariable Integer count,
                                                     HttpServletRequest request) {
        CommonUtils.validateTokenAndDataRequestedUser(request, userID);
        List<HealthCheckupResult> latestCheckupsForUser = healthCheckupService.getLatestCheckupsForUser(userID, count);
        return new ResponseEntity<>(latestCheckupsForUser, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateHealthCheckup(@RequestBody HealthCheckupResult healthCheckupResult, HttpServletRequest request) {
        CommonUtils.validateTokenAndDataRequestedUser(request, healthCheckupResult.getUserID());
        try {
            HealthCheckupResult checkupResultToUpdate = healthCheckupService.getHealthCheckupResult(healthCheckupResult.getCheckupID());

            for (HealthCheckupValue healthCheckupValue : healthCheckupResult.getCheckupValues()) {
                healthCheckupValue.setHealthCheckupResult(healthCheckupResult);

                for (HealthCheckupValue checkupValueToUpdate : checkupResultToUpdate.getCheckupValues()) {
                    if (healthCheckupValue.equals(checkupValueToUpdate)) {
                        checkupValueToUpdate.setValue(healthCheckupValue.getValue());
                    }
                }
            }
            HealthCheckupResult updatedHealthCheckupResult = healthCheckupService.updateHealthCheckupResult(checkupResultToUpdate);
            updateHeightWeightInUserProfile(healthCheckupResult.getUserID());
            return new ResponseEntity<>(updatedHealthCheckupResult, HttpStatus.OK);
        } catch (HealthPredictionAppException appException) {
            return appException.toResponseEntity();
        }
    }

    private void updateHeightWeightInUserProfile(Integer userID) {
        try {
            User user = userService.findUserByID(userID);
            HealthCheckupResult latestCheckResult = healthCheckupService.getLatestCheckUpForUser(userID);
            Optional<HealthCheckupValue> latestHeight = latestCheckResult.getCheckupValues().stream().filter(
                    healthCheckupValue -> healthCheckupValue.getCheckupItem().equals(HEALTH_CHECKUP_ITEM_HEIGHT)).findAny();
            Optional<HealthCheckupValue> latestWeight = latestCheckResult.getCheckupValues().stream().filter(
                    healthCheckupValue -> healthCheckupValue.getCheckupItem().equals(HEALTH_CHECKUP_ITEM_WEIGHT)).findAny();

            boolean updateUser = latestHeight.isPresent() && user.setHeight(latestHeight.get().getValue());
            if (latestWeight.isPresent() && user.setWeight(latestWeight.get().getValue())) {
                updateUser = true;
            }

            if (updateUser) {
                userService.updateUser(user);
            }
        } catch (HealthPredictionAppException appException) {
            appException.printStackTrace();
        }
    }

}
