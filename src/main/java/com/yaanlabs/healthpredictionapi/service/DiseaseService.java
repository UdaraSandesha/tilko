package com.yaanlabs.healthpredictionapi.service;

import com.yaanlabs.healthpredictionapi.model.Disease;
import com.yaanlabs.healthpredictionapi.repository.DiseaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class DiseaseService {

    @Autowired
    private DiseaseRepository diseaseRepository;

    public List<Disease> getDiseasesWithName(List<String> diseaseNames) {
        return diseaseRepository.findByDiseaseNameIn(diseaseNames);
    }
}
