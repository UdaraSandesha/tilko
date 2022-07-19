package com.yaanlabs.healthpredictionapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthResponse extends APIAppResponse {

    @JsonProperty("user_id")
    private Integer userID;

    private String token;

    public AuthResponse(String messageCode, Integer userID, String token) {
        super(messageCode);
        this.userID = userID;
        this.token = token;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
