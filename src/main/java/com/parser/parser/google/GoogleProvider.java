package com.parser.parser.google;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.parser.parser.exceptions.GoogleServiceException;
import com.parser.parser.utils.Constants;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GoogleProvider {
    @Value("#{'${integration.email.scope.drive}'.split(',')}")
    List<String> scopes;
    @Value("#{'${integration.google.credentials}'}")
    String credentials;
    @Value("#{'${integration.google.secret}'}")
    String secret;
    @Value("#{'${integration.google.client.id}'}")
    String clientId;
    @Value("#{'${integration.google.refresh-token}'}")
    String refreshToken;
    NetHttpTransport httpTransport;
    JsonFactory jsonFactory;
    GoogleAuthorizationCodeFlow flow;

    @PostConstruct
    void init() {
        try {
            this.httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            this.jsonFactory = JacksonFactory.getDefaultInstance();
            this.flow = getFlow();
        } catch (Exception e) {
            log.error("Can't initialize Gmail provider", e);
        }
    }


    public Drive getDriveService() {
        try {
            var credential = exchangeCode();
            return new Drive.Builder(httpTransport, jsonFactory, credential).build();
        } catch (Exception e) {
            log.error("Can't connect to google drive. {}", e.getMessage());
            return null;
        }
    }

    private GoogleAuthorizationCodeFlow getFlow() throws IOException {
        var in = new ClassPathResource(credentials).getInputStream();
        var clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));
        return new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, clientSecrets, scopes)
                .setAccessType("offline").build();
    }

    private Credential exchangeCode() throws IOException {
        GoogleTokenResponse response = new GoogleTokenResponse();
        response.setAccessToken(convertRefreshTokenToAccessToken(refreshToken, clientId, secret));
        return flow.createAndStoreCredential(response, Constants.CURRENT_USER_ID);
    }

    private String convertRefreshTokenToAccessToken(String refreshToken, String clientId, String clientSecret) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> request = new HttpEntity<>(getBody(clientId, clientSecret, refreshToken), httpHeaders);
        String response = restTemplate.postForObject(Constants.GMAIL_TOKEN_URL, request, String.class);
        ObjectMapper objectMapper = new JsonMapper();
        try {
            return objectMapper.readTree(response).get("access_token").asText();
        } catch (JsonProcessingException e) {
            throw new GoogleServiceException("Can't get access token from refresh token", e);
        }
    }

    private String getBody(String clientId, String clientSecret, String refreshToken) {
        return "client_id="
                + clientId
                + "&"
                + "client_secret="
                + clientSecret
                + "&"
                + "grant_type=refresh_token"
                + "&"
                + "refresh_token="
                + refreshToken;
    }
}
