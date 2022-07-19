package com.yaanlabs.healthpredictionapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "health_checkup_value")
public class HealthCheckupValue {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Integer id;

    @Column(name = "checkup_item")
    @JsonProperty("checkup_item")
    private String checkupItem;

    private Double value;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "checkup_id", nullable = false)
    @JsonIgnore
    private HealthCheckupResult healthCheckupResult;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public HealthCheckupResult getHealthCheckupResult() {
        return healthCheckupResult;
    }

    public void setHealthCheckupResult(HealthCheckupResult healthCheckupResult) {
        this.healthCheckupResult = healthCheckupResult;
    }

    public String getCheckupItem() {
        return checkupItem;
    }

    public void setCheckupItem(String checkupItem) {
        this.checkupItem = checkupItem;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HealthCheckupValue that = (HealthCheckupValue) o;
        return Objects.equals(healthCheckupResult, that.healthCheckupResult) && Objects.equals(checkupItem, that.checkupItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(healthCheckupResult, checkupItem);
    }
}
