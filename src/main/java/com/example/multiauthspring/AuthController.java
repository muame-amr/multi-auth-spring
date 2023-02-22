package com.example.multiauthspring;

import org.springframework.web.bind.annotation.GetMapping;

public class AuthController {

    @GetMapping("/api/ping")
    public String getPing() {
        return "OK";
    }
}
