package com.yaanlabs.healthpredictionapi.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.yaanlabs.healthpredictionapi.HealthPredictionAppException;
import com.yaanlabs.healthpredictionapi.auth.OAuthProviderProperties;
import com.yaanlabs.healthpredictionapi.auth.OAuthRegistrationProperties;
import com.yaanlabs.healthpredictionapi.model.AuthResponse;
import com.yaanlabs.healthpredictionapi.model.OAuthResponse;
import com.yaanlabs.healthpredictionapi.model.User;
import com.yaanlabs.healthpredictionapi.service.Translator;
import com.yaanlabs.healthpredictionapi.service.UserService;
import com.yaanlabs.healthpredictionapi.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.sql.Date;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login/oauth")
public class OAuthController {

    private static final String LOGIN_PROVIDER_KAKAO = "kakao";
    private static final String LOGIN_PROVIDER_NAVER = "naver";

    @Autowired
    private UserService userService;

    @Autowired
    OAuthRegistrationProperties oAuthRegistrationProperties;

    @Autowired
    OAuthProviderProperties oAuthProviderProperties;

    @Value("${application.oauth2.result.landing.page}")
    String oauth2ResultPageUri;

    @RequestMapping(value = "/kakao")
    public ResponseEntity<?> kakaoRedirect(@RequestParam String code, @RequestParam String state) {
        try {
            ResponseEntity<OAuthResponse> accessTokenResponse = getAccessToken(oAuthRegistrationProperties.getKakao(),
                                                                oAuthProviderProperties.getKakao().getTokenURI(), code);
            OAuthResponse accessTokenResponseBody = accessTokenResponse.getBody();

            if (!accessTokenResponse.getStatusCode().equals(HttpStatus.OK) || accessTokenResponseBody == null) {
                return onAuthProviderLoginFail(getQueryParamsForLoginFailure(Translator.toLocale("user.signin.kakao.userinfo.failed")));
            }

            ResponseEntity<ObjectNode> userInfoResponse = getUserInfo(oAuthProviderProperties.getKakao().getUserInfoURI(),
                                                                                accessTokenResponseBody.getAccessToken());
            ObjectNode userInfoResponseBody = userInfoResponse.getBody();
            if (userInfoResponse.getStatusCode().equals(HttpStatus.OK) && userInfoResponseBody != null) {
                User user = extractUserInfoFromKakao(userInfoResponseBody);
                return onAuthProviderLoginSuccess(user);
            }

            return onAuthProviderLoginFail(getQueryParamsForLoginFailure(Translator.toLocale("user.signin.kakao.userinfo.failed")));
        } catch (HealthPredictionAppException appException) {
            return onAuthProviderLoginFail(getQueryParamsForLoginFailure(appException.getMessage()));
        }
    }

    @RequestMapping(value = "/naver", params = {"code", "state"})
    public ResponseEntity<?> redirectOnSuccessfulNaverLogin(@RequestParam String code, @RequestParam String state) {
        try {
            ResponseEntity<OAuthResponse> accessTokenResponse = getAccessToken(oAuthRegistrationProperties.getNaver(),
                                                                oAuthProviderProperties.getNaver().getTokenURI(), code);
            OAuthResponse accessTokenResponseBody = accessTokenResponse.getBody();

            if (!accessTokenResponse.getStatusCode().equals(HttpStatus.OK) || accessTokenResponseBody == null) {
                return onAuthProviderLoginFail(getQueryParamsForLoginFailure(Translator.toLocale("user.signin.naver.userinfo.failed")));
            }

            ResponseEntity<ObjectNode> userInfoResponse = getUserInfo(oAuthProviderProperties.getNaver().getUserInfoURI(),
                                                                                accessTokenResponseBody.getAccessToken());
            ObjectNode userInfoResponseBody = userInfoResponse.getBody();
            if (userInfoResponse.getStatusCode().equals(HttpStatus.OK) && userInfoResponseBody != null) {
                User user = extractUserInfoFromNaver(userInfoResponseBody);
                return onAuthProviderLoginSuccess(user);
            }

            return onAuthProviderLoginFail(getQueryParamsForLoginFailure(Translator.toLocale("user.signin.naver.userinfo.failed")));
        } catch (HealthPredictionAppException appException) {
            return onAuthProviderLoginFail(getQueryParamsForLoginFailure(appException.getMessage()));
        }
    }

    @RequestMapping(value = "/naver", params = {"error", "error_description", "state"})
    public ResponseEntity<?> redirectOnSuccessfulNaverLogin(@RequestParam String error, @RequestParam("error_description") String errorDescription,
                                                            @RequestParam String state) {
        return onAuthProviderLoginFail(getQueryParamsForLoginFailure(errorDescription));
    }

    private ResponseEntity<OAuthResponse> getAccessToken(OAuthRegistrationProperties.ClientProperties registrationClientProperties,
                                                         String accessTokenURIString, String code) {
        URI accessTokenURI = CommonUtils.addPathToURI(accessTokenURIString, "");  // Convert String to URI
        HttpHeaders tokenRequestHeader = CommonUtils.createURLEncodedContentTypeHeader();

        MultiValueMap<String, String> tokenRequestBody = new LinkedMultiValueMap<>();
        tokenRequestBody.add("client_id", registrationClientProperties.getClientID());
        tokenRequestBody.add("client_secret", registrationClientProperties.getClientSecret());
        tokenRequestBody.add("grant_type", registrationClientProperties.getAuthorizationGrantType());
        tokenRequestBody.add("redirect_uri", registrationClientProperties.getRedirectURI());
        tokenRequestBody.add("code", code);

        return CommonUtils.makeRESTCall(accessTokenURI, HttpMethod.POST, tokenRequestHeader, tokenRequestBody, OAuthResponse.class);
    }

    private ResponseEntity<ObjectNode> getUserInfo(String userInfoURIString, String accessToken) {
        URI userInfoURI = CommonUtils.addPathToURI(userInfoURIString, "");    // Convert String to URI
        HttpHeaders userInfoRequestHeader = CommonUtils.createURLEncodedContentTypeHeader(Collections.singletonMap(
                "Authorization", getAuthorizationHeaderValue(accessToken)));
        return CommonUtils.makeRESTCall(userInfoURI, HttpMethod.POST, userInfoRequestHeader, null, ObjectNode.class);
    }

    private String getAuthorizationHeaderValue(String token) {
        return "Bearer " + token;
    }

    private User extractUserInfoFromKakao(ObjectNode userInfoRequestResult) throws HealthPredictionAppException {
        User user = null;
        if (userInfoRequestResult.has("kakao_account")) {
            JsonNode userInfoNode = userInfoRequestResult.get("kakao_account");
            user = extractCommonUserInfo(userInfoNode);
            user.setName(getFieldValue(userInfoNode.get("profile"), "nickname"));
            String profileImage = getFieldValue(userInfoNode.get("profile"), "profile_image_url");
            if (profileImage != null) {
                user.setProfileImage(CommonUtils.encodeImageInBas64(profileImage));
            }
            user.setProvider(LOGIN_PROVIDER_KAKAO);
        }

        if (user == null) {
            throw new HealthPredictionAppException("user.signin.kakao.userinfo.failed");
        }
        return user;
    }

    private User extractUserInfoFromNaver(ObjectNode userInfoRequestResult) throws HealthPredictionAppException {
        User user = null;
        String message = null;
        if (userInfoRequestResult.get("resultcode").textValue().equals("00")) {
            if (userInfoRequestResult.has("response")) {
                JsonNode userInfoNode = userInfoRequestResult.get("response");
                user = extractCommonUserInfo(userInfoNode);
                user.setName(getFieldValue(userInfoNode, "name"));

                String mobileNo = getFieldValue(userInfoNode, "mobile_e164");
                if (mobileNo == null || mobileNo.isEmpty()) {
                    mobileNo = getFieldValue(userInfoNode, "mobile");
                }
                user.setMobileNo(mobileNo);

                String birthDay = getFieldValue(userInfoNode, "birthday");
                String birthYear = getFieldValue(userInfoNode, "birthyear");
                if (birthYear != null && birthDay != null) {
                    user.setBirthday(Date.valueOf(birthYear + "-" + birthDay));
                }

                String profileImage = getFieldValue(userInfoNode, "profile_image");
                if (profileImage != null) {
                    user.setProfileImage(CommonUtils.encodeImageInBas64(profileImage));
                }
                user.setProvider(LOGIN_PROVIDER_NAVER);
            }
        } else {
            message = userInfoRequestResult.get("message").textValue();
        }

        if (user == null) {
            throw new HealthPredictionAppException("user.signin.naver.failed", message);
        }
        return user;
    }

    private User extractCommonUserInfo(JsonNode userInfoNode) throws HealthPredictionAppException {
        String email = getFieldValue(userInfoNode, "email");
        if (email == null) {
            throw new HealthPredictionAppException("user.signin.info.email.failed");
        }
        User user = getUserWithEmail(email);

        String gender = CommonUtils.getGenderFromAuthProvider(getFieldValue(userInfoNode, "gender"));
        if (gender != null) {
            user.setGender(gender);
        }
        return user;
    }

    private User getUserWithEmail(String email) {
        try {
            return userService.findUserByEmail(email);
        } catch (HealthPredictionAppException appException) {
            User user = new User();
            user.setEmail(email);
            user.setPassword("null");
            return user;
        }
    }

    private String getFieldValue(JsonNode jsonNode, String fieldName) {
        String fieldValue = null;
        if (jsonNode.has(fieldName)) {
            fieldValue = jsonNode.get(fieldName).textValue();
        }
        return fieldValue;
    }

    private ResponseEntity<?> onAuthProviderLoginFail(Map<String, String> resultParameters) {
        URI resultPageURI = CommonUtils.addQueryParamsToURI(this.oauth2ResultPageUri, resultParameters);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(resultPageURI);
        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
    }

    private ResponseEntity<?> onAuthProviderLoginSuccess(User user) {
        AuthResponse authResponse = userService.onSuccessfulSignin(user);
        URI resultPageURI = CommonUtils.addQueryParamsToURI(this.oauth2ResultPageUri, getQueryParamsForLoginSuccess(authResponse));
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(resultPageURI);
        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
    }

    private Map<String, String> getQueryParamsForLoginFailure(String errorMessage) {
        Map<String, String> queryParameters = new HashMap<>();
        queryParameters.put("error", errorMessage);
        return queryParameters;
    }

    private Map<String, String> getQueryParamsForLoginSuccess(AuthResponse authResponse) {
        Map<String, String> queryParameters = new HashMap<>();
        queryParameters.put("token", authResponse.getToken());
        queryParameters.put("userId", Integer.toString(authResponse.getUserID()));
        return queryParameters;
    }
}
