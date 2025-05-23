package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class InterestListController {

    @PostMapping("/interest-list")
    public ResponseEntity<Map<String, String>> addToInterestList(@RequestBody Map<String, Object> formData) {
        // Here you would typically save this data to a database
        // For now, we'll just log it and return a success response
        System.out.println("Received interest list submission: " + formData);
        
        return ResponseEntity.ok(Map.of("status", "success", "message", "Your information has been received"));
    }
}
