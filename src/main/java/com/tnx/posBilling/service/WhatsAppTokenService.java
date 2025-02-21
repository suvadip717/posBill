package com.tnx.posBilling.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tnx.posBilling.model.Token;
import com.tnx.posBilling.repository.TokenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;

@Service
public class WhatsAppTokenService {

    @Autowired
    private TokenRepository tokenRepository;

    // @Value("${ACCESS_TOKEN}")
    // private String ACCESS_TOKEN;
    @Value("${APP_ID}")
    private String APP_ID;
    @Value("${APP_SECRET}")
    private String APP_SECRET;

    // @Scheduled(fixedRate = 5184000000L)
    // @Scheduled(cron = "0 * * * * ?")
    @Scheduled(cron = "0 * * * */2 ?")
    public void refreshAccessToken() {

        // https://graph.facebook.com/v22.0/oauth/access_token?grant_type=fb_exchange_token&client_id=1715159599062970&client_secret=33b105e854945d3cae4b6d7e3907158c&fb_exchange_token=EAAYX7bain7oBOZB1h25cxaHJSh8qWsJSVcjvdAxcfIfsOzviMXWwUGgPDnYM3rlwJmjLshdI5mseviK5Evodd7sVokuxQ5fwHtKM6msXkXvzNWTHA3ZAGLfOCzO9V743LMGGRE2NKzY617wMOcd2tfrosmURZAZCzRcm8xwAEViIKUGJXyL9Ax3k

        Token existToken = tokenRepository.findById(1).get();
        try {
            String url = "https://graph.facebook.com/v22.0/oauth/access_token?" +
                    "grant_type=fb_exchange_token&client_id=" + APP_ID +
                    "&client_secret=" + APP_SECRET +
                    "&fb_exchange_token=" + existToken.getValue();

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            // Convert JSON string response to JsonNode
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());

            // Extract access_token
            String accessToken = jsonNode.get("access_token").asText();
            System.out.println("Access Token: " + accessToken);
            existToken.setValue(accessToken);
            tokenRepository.save(existToken);

        } catch (Exception e) {
            e.printStackTrace(); // Print error details
            System.out.println("Error extracting access token!");
        }
    }
}
