package com.yaanlabs.healthpredictionapi.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Base64;
import java.util.Map;

public class CommonUtils {

    private static final Log logger = LogFactory.getLog(CommonUtils.class);

    public static final String GENDER_MALE = "male";
    public static final String GENDER_FEMALE = "female";

    public static final String REQUEST_ATTRIBUTE_TOKEN_USER_ID = "com.yaanlabs.balancebiomapi.token.user.id";

    public static HttpHeaders createURLEncodedContentTypeHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return httpHeaders;
    }

    public static HttpHeaders createURLEncodedContentTypeHeader(Map<String, String> additionalHeaders) {
        HttpHeaders httpHeaders = createURLEncodedContentTypeHeader();
        additionalHeaders.forEach(httpHeaders::set);
        return httpHeaders;
    }

    public static <T> ResponseEntity<T> makeRESTCall(URI requestURI, HttpMethod httpMethod, HttpHeaders headers,
                                                     MultiValueMap<String, String> body, Class<T> responseType) {
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        return new RestTemplate().exchange(requestURI, httpMethod, request, responseType);
    }

    public static URI addPathToURI(String uri, String path) {
        return UriComponentsBuilder.fromUriString(uri).path(path).build().toUri();
    }

    public static URI addQueryParamsToURI(String url, Map<String, String> queryParams) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(url);
        queryParams.forEach((parameterName, parameterValue) ->
                uriComponentsBuilder.queryParam(parameterName, UriUtils.encode(parameterValue, StandardCharsets.UTF_8)));
        return uriComponentsBuilder.build().toUri();
    }

    public static boolean isNonEmptyDifferentString(String newString, String oldString) {
        return newString != null && !newString.isEmpty() && !newString.equals(oldString);
    }

    public static boolean isDifferentDouble(Double newDoubleValue, Double oldDoubleValue) {
        return newDoubleValue != null && !newDoubleValue.equals(oldDoubleValue);
    }

    public static int getIntGender(String gender) {
        if (gender.equalsIgnoreCase("male"))
            return 1;
        else
            return 2;
    }

    public static Integer getAgeFromBirthday(Date birthday) {
        LocalDate today = LocalDate.now();
        return Period.between(birthday.toLocalDate(), today).getYears();
    }

    public static String getGenderFromAuthProvider(String genderValueFromAuthProvider) {
        if (genderValueFromAuthProvider == null)
            return null;

        if (genderValueFromAuthProvider.startsWith("M") || genderValueFromAuthProvider.startsWith("m")) {
            return CommonUtils.GENDER_MALE;
        } else if (genderValueFromAuthProvider.equals("F") || genderValueFromAuthProvider.startsWith("f")) {
            return CommonUtils.GENDER_FEMALE;
        }
        return null;
    }

    public static String encodeImageInBas64(String imageURL) {
        try {
            byte[] imageBytes = downloadFile(imageURL);
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException exception) {
            logger.warn("Image will not be encoded to Base64 as it failed to download.", exception);
            return null;
        }
    }

    public static byte[] downloadFile(String url) throws IOException {
        URLConnection urlConnection = new URL(url).openConnection();
        urlConnection.setConnectTimeout(5000);
        urlConnection.setReadTimeout(5000);
        urlConnection.connect();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.copy(urlConnection.getInputStream(), outputStream);
        return outputStream.toByteArray();
    }

    public static boolean isUniqueKeyIntegrityViolation(DataIntegrityViolationException integrityViolationException,
                                                        String uniqueKeyName) {
        Throwable throwable = integrityViolationException.getCause();
        while (throwable != null && !(throwable instanceof SQLIntegrityConstraintViolationException)) {
            throwable = throwable.getCause();
        }

        return throwable != null && throwable.getMessage().endsWith(uniqueKeyName);
    }

    public static void validateTokenAndDataRequestedUser(HttpServletRequest request, Integer dataRequestedUserID) {
        Object tokenUserID = request.getAttribute(REQUEST_ATTRIBUTE_TOKEN_USER_ID);
        if (tokenUserID == null || !tokenUserID.equals(dataRequestedUserID))
            throw new AccessDeniedException("Not allowed to access other user's data");
    }

}
