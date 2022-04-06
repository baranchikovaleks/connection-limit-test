package com.example.connectionlimit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    public static final MethodLock lock = new MethodLock();


    @GetMapping("/test")
    public ResponseEntity<String> test() {
        lock.enter();
        return ResponseEntity.ok("OK");
    }
}
