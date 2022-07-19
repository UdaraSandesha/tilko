package com.yaanlabs.healthpredictionapi.service;

import com.yaanlabs.healthpredictionapi.model.HealthCheckupItem;
import com.yaanlabs.healthpredictionapi.repository.HealthCheckupItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class HealthCheckupItemService {

    @Autowired
    HealthCheckupItemRepository healthCheckupItemRepository;

    public List<HealthCheckupItem> getAllCheckupItems() {
        return healthCheckupItemRepository.findAll();
    }

}
