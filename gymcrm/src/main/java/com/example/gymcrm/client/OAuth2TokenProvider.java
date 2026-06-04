package com.example.gymcrm.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Map;

@Component
@Slf4j
public class OAuth2TokenProvider {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${auth-server.token-url:http://localhost:9000/oauth2/token}")
    private String tokenUrl;

    @Value("${auth-server.client-id:gymcrm}")
    private String clientId;

    @Value("${auth-server.client-secret:gymcrm-secret}")
    private String clientSecret;

    private String cachedToken;
    private Instant tokenExpiry = Instant.MIN;

    public synchronized String getToken() {
        if (cachedToken == null || Instant.now().isAfter(tokenExpiry)) {
            cachedToken = fetchNewToken();
            tokenExpiry = Instant.now().plusSeconds(3500);
        }
        return cachedToken;
    }

    @SuppressWarnings("unchecked")
    private String fetchNewToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("scope", "workload.write");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
        String token = (String) response.getBody().get("access_token");
        log.info("Obtained new OAuth2 token from auth-server");
        return token;
    }
}