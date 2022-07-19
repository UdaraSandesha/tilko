package com.yaanlabs.healthpredictionapi.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "spring.security.oauth2.client.provider")
@Configuration
public class OAuthProviderProperties {

    private OAuthProviderProperties.ClientProperties kakao;

    private OAuthProviderProperties.ClientProperties naver;

    public OAuthProviderProperties.ClientProperties getKakao() {
        return kakao;
    }

    public void setKakao(OAuthProviderProperties.ClientProperties kakao) {
        this.kakao = kakao;
    }

    public OAuthProviderProperties.ClientProperties getNaver() {
        return naver;
    }

    public void setNaver(OAuthProviderProperties.ClientProperties naver) {
        this.naver = naver;
    }

    public static class ClientProperties {

        private String tokenURI;

        private String userInfoURI;

        public String getTokenURI() {
            return tokenURI;
        }

        public void setTokenURI(String tokenURI) {
            this.tokenURI = tokenURI;
        }

        public String getUserInfoURI() {
            return userInfoURI;
        }

        public void setUserInfoURI(String userInfoURI) {
            this.userInfoURI = userInfoURI;
        }

    }

}
