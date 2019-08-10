package com.salon.www.salonapi.controller;

import com.google.gson.Gson;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    private static final Gson gson = new Gson();
    @GetMapping("/home")
    public ResponseEntity<?> getHome() {
        return ResponseEntity.ok(gson.toJson("Hello from Java"));
    }
}
