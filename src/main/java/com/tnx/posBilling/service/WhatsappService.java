package com.tnx.posBilling.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tnx.posBilling.model.Token;
import com.tnx.posBilling.repository.TokenRepository;

@Service
public class WhatsappService {
    // @Autowired
    // private TokenRepository tokenRepository;
    // private final String PHONE_NUMBER_ID = "your-phone-number-id";
    @Value("${ACCESS_TOKEN}")
    // Token accessToken = tokenRepository.findById(1).get();
    private String ACCESS_TOKEN;
    @Value("${API_URL}")
    private String API_URL;

    public void sendWhatsAppMessage(String to) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // headers.setBearerAuth(accessToken.getValue());
        headers.setBearerAuth(ACCESS_TOKEN);

        Map<String, Object> body = Map.of(
                "messaging_product", "whatsapp",
                "to", "91" + to,
                "type", "template",
                "template", Map.of("name", "hello_world", "language", Map.of("code", "en_US")));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, request, String.class);
        System.out.println("Response: " + response.getBody());

    }
}
