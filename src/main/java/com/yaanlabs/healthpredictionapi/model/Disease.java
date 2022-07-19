package com.yaanlabs.healthpredictionapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "disease")
public class Disease {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "disease_id")
    private Integer diseaseID;

    @Column(name = "disease_name")
    @JsonProperty("disease_name")
    private String diseaseName;

    private String cause;

    @Column(name = "recommended_food")
    @JsonProperty("recommended_food")
    private String recommendedFood;

    @Column(name = "caution_food")
    @JsonProperty("caution_food")
    private String cautionFood;

    @Column(name = "recommendation_reason")
    @JsonProperty("recommendation_reason")
    private String recommendationReason;

    @Column(name = "be_careful_reason")
    @JsonProperty("be_careful_reason")
    private String beCarefulReason;

    public Integer getDiseaseID() {
        return diseaseID;
    }

    public void setDiseaseID(Integer diseaseID) {
        this.diseaseID = diseaseID;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getRecommendedFood() {
        return recommendedFood;
    }

    public void setRecommendedFood(String recommendedFood) {
        this.recommendedFood = recommendedFood;
    }

    public String getCautionFood() {
        return cautionFood;
    }

    public void setCautionFood(String cautionFood) {
        this.cautionFood = cautionFood;
    }

    public String getRecommendationReason() {
        return recommendationReason;
    }

    public void setRecommendationReason(String recommendationReason) {
        this.recommendationReason = recommendationReason;
    }

    public String getBeCarefulReason() {
        return beCarefulReason;
    }

    public void setBeCarefulReason(String beCarefulReason) {
        this.beCarefulReason = beCarefulReason;
    }
}
