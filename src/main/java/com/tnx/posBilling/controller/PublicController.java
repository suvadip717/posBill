package com.tnx.posBilling.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/public")
public class PublicController {
    @GetMapping("/health-check")
    public String helthCheck() {
        return "Ok";
    }
}
