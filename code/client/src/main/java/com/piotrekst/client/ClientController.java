package com.piotrekst.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
class ClientController {

    private final Logger LOGGER = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/client-endpoint")
    private ResponseEntity<String> clientEndpoint() {
        LOGGER.info("Received request. Calling server via mTLS");
        String response = restTemplate.getForObject("https://localhost:9000/secured-endpoint", String.class);
        LOGGER.info("Received response from external server {response={}}", response);
        return ResponseEntity.ok(response);
    }
}
