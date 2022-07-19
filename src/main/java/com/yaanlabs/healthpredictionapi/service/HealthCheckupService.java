package com.yaanlabs.healthpredictionapi.service;

import com.yaanlabs.healthpredictionapi.HealthPredictionAppException;
import com.yaanlabs.healthpredictionapi.model.HealthCheckupResult;
import com.yaanlabs.healthpredictionapi.repository.HealthCheckupResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HealthCheckupService {

    @Autowired
    private HealthCheckupResultRepository healthCheckupResultRepository;

    public void addHealthCheckupResult(HealthCheckupResult healthCheckupResult) {
        healthCheckupResultRepository.save(healthCheckupResult);
    }

    public HealthCheckupResult getHealthCheckupResult(Integer checkupID) throws HealthPredictionAppException {
        Optional<HealthCheckupResult> healthCheckupResult = healthCheckupResultRepository.findById(checkupID);
        if (healthCheckupResult.isPresent()) {
            return healthCheckupResult.get();
        } else {
            throw new HealthPredictionAppException("checkup.not.found");
        }
    }

    public List<HealthCheckupResult> getLatestCheckupsForUser(Integer userID, Integer count) {
        return healthCheckupResultRepository.findByUserIDOrderByCheckupDateDesc(userID, PageRequest.of(0, count));
    }

    public HealthCheckupResult updateHealthCheckupResult(HealthCheckupResult healthCheckupResult) {
        return healthCheckupResultRepository.save(healthCheckupResult);
    }

    public HealthCheckupResult getLatestCheckUpForUser(Integer userID) {
        return healthCheckupResultRepository.findFirstByUserIDOrderByCheckupDateDesc(userID);
    }
}
