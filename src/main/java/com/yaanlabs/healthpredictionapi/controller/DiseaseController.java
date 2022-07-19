package com.yaanlabs.healthpredictionapi.controller;

import com.yaanlabs.healthpredictionapi.model.Disease;
import com.yaanlabs.healthpredictionapi.service.DiseaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disease")
public class DiseaseController {

    @Autowired
    DiseaseService diseaseService;

    @PostMapping("/find")
    public ResponseEntity<?> addHealthCheckup(@RequestBody List<String> diseaseNames) {
        List<Disease> diseaseList = diseaseService.getDiseasesWithName(diseaseNames);
        return new ResponseEntity<>(diseaseList, HttpStatus.OK);
    }
}
