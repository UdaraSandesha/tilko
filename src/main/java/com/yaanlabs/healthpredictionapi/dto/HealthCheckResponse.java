package com.yaanlabs.healthpredictionapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HealthCheckResponse {

    private String Year;
    private String CheckUpDate;
    private String Code;
    private String Location;
    private String Description;
    private List<Object> Inspections;
    private String message;

}
