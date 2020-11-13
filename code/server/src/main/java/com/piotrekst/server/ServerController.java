package com.piotrekst.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ServerController {

    private final Logger LOGGER = LoggerFactory.getLogger(ServerController.class);

    @GetMapping("/secured-endpoint")
    public ResponseEntity<String> securedEndpoint() {
        LOGGER.info("Called secured service successfully");
        return ResponseEntity.ok("Hello from server!");
    }
}

