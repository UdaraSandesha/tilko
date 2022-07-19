package com.yaanlabs.healthpredictionapi.repository;

import com.yaanlabs.healthpredictionapi.model.HealthCheckupResult;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HealthCheckupResultRepository extends JpaRepository<HealthCheckupResult, Integer> {

    HealthCheckupResult findFirstByUserIDOrderByCheckupDateDesc(Integer userID);

    List<HealthCheckupResult> findByUserIDOrderByCheckupDateDesc(Integer userId, Pageable pageable);

}
