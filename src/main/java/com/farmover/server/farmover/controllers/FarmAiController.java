package com.farmover.server.farmover.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@CrossOrigin
public class FarmAiController {

    @Value("${spring.ai.vertex.ai.api-key}")
    private String apiKey;

    @GetMapping("/key")
    public ResponseEntity<String> getApiKey() {
        return new ResponseEntity<String>(apiKey, HttpStatus.OK);
    }

}
