package com.yaanlabs.healthpredictionapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "health_checkup_item")
public class HealthCheckupItem {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "checkup_item_id")
    private Integer checkupItemID;

    @Column(name = "checkup_item")
    @JsonProperty("checkup_item")
    private String checkupItem;

    @Column(name = "short_name")
    @JsonProperty("short_name")
    private String shortName;

    @Column(name = "related_health_function")
    @JsonProperty("related_health_function")
    private String relatedHealthFunction;

    @Column(name = "food_ingredients", columnDefinition = "TEXT")
    @Basic(fetch = FetchType.LAZY)
    @JsonProperty("food_ingredients")
    private String foodIngredients;

    @Column(name = "effect_of_food", columnDefinition = "TEXT")
    @Basic(fetch = FetchType.LAZY)
    @JsonProperty("effect_of_food")
    private String effectOfFood;

    public String getCheckupItem() {
        return checkupItem;
    }

    public String getShortName() {
        return shortName;
    }
}
