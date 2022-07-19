package com.yaanlabs.healthpredictionapi.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration")
@Configuration
public class OAuthRegistrationProperties {

    private ClientProperties kakao;

    private ClientProperties naver;

    public ClientProperties getKakao() {
        return kakao;
    }

    public void setKakao(ClientProperties kakao) {
        this.kakao = kakao;
    }

    public ClientProperties getNaver() {
        return naver;
    }

    public void setNaver(ClientProperties naver) {
        this.naver = naver;
    }

    public static class ClientProperties {

        private String clientID;

        private String clientSecret;

        private String authorizationGrantType;

        private String redirectURI;

        public String getClientID() {
            return clientID;
        }

        public void setClientID(String clientID) {
            this.clientID = clientID;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }

        public String getAuthorizationGrantType() {
            return authorizationGrantType;
        }

        public void setAuthorizationGrantType(String authorizationGrantType) {
            this.authorizationGrantType = authorizationGrantType;
        }

        public String getRedirectURI() {
            return redirectURI;
        }

        public void setRedirectURI(String redirectURI) {
            this.redirectURI = redirectURI;
        }

    }

}
