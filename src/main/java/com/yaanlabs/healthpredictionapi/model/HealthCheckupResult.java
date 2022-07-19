package com.yaanlabs.healthpredictionapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.sql.Date;
import java.util.*;

@Entity
@Table(name = "health_checkup_result")
public class HealthCheckupResult {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "checkup_id")
    @JsonProperty("checkup_id")
    private Integer checkupID;

    @Column(name = "user_id")
    @JsonProperty("user_id")
    private Integer userID;

    @Column(name = "checkup_date")
    @JsonProperty("checkup_date")
    private Date checkupDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "healthCheckupResult")
    @JsonProperty("health_checkup_values")
    private Set<HealthCheckupValue> checkupValues = new HashSet<>();

    public Integer getCheckupID() {
        return checkupID;
    }

    public void setCheckupID(Integer recordID) {
        this.checkupID = recordID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Date getCheckupDate() {
        return checkupDate;
    }

    public void setCheckupDate(Date checkupDate) {
        this.checkupDate = checkupDate;
    }

    public Set<HealthCheckupValue> getCheckupValues() {
        return checkupValues;
    }

    public void setCheckupValues(List<HealthCheckupValue> healthCheckupValues) {
        for (HealthCheckupValue healthCheckupValue : healthCheckupValues) {
            addCheckupValue(healthCheckupValue);
        }
    }

    public void addCheckupValue(HealthCheckupValue healthCheckupValue) {
        this.checkupValues.add(healthCheckupValue);
        healthCheckupValue.setHealthCheckupResult(this);
    }

    public void removeCheckupValue(HealthCheckupValue healthCheckupValue) {
        this.checkupValues.remove(healthCheckupValue);
        healthCheckupValue.setHealthCheckupResult(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HealthCheckupResult that = (HealthCheckupResult) o;
        return Objects.equals(checkupID, that.checkupID) && Objects.equals(userID, that.userID) &&
                Objects.equals(checkupDate.toString(), that.checkupDate.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(checkupID, userID, checkupDate);
    }
}
