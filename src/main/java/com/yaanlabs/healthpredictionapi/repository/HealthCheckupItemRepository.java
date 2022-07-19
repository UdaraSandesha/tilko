package com.yaanlabs.healthpredictionapi.repository;

import com.yaanlabs.healthpredictionapi.model.HealthCheckupItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthCheckupItemRepository extends JpaRepository<HealthCheckupItem, Integer> {

}
