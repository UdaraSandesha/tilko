package com.yaanlabs.healthpredictionapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OAuthResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("expires_in")
    private int expires;

    private String scope;

    @JsonProperty("refresh_token_expires_in")
    private int refreshExpire;

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public int getExpires() {
        return expires;
    }

    public String getScope() {
        return scope;
    }

    public int getRefreshExpire() {
        return refreshExpire;
    }

}
