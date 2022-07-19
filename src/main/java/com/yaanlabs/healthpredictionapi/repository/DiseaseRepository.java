package com.yaanlabs.healthpredictionapi.repository;

import com.yaanlabs.healthpredictionapi.model.Disease;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiseaseRepository extends JpaRepository<Disease, Integer> {

    List<Disease> findByDiseaseNameIn(List<String> diseaseNames);

}
