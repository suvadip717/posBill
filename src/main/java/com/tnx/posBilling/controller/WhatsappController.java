package com.tnx.posBilling.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tnx.posBilling.service.WhatsAppTokenService;
import com.tnx.posBilling.service.WhatsappService;

@RestController
@CrossOrigin
@RequestMapping("/whatsapp")
public class WhatsappController {
    @Autowired
    private WhatsappService whatsappService;

    @Autowired
    private WhatsAppTokenService whatsAppTokenService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestParam String to) {
        whatsappService.sendWhatsAppMessage(to);
        return ResponseEntity.ok("Message sent!");
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<String> refreshToken() {
        whatsAppTokenService.refreshAccessToken();
        return new ResponseEntity<>("Token is refresh", HttpStatus.OK);
    }
}
