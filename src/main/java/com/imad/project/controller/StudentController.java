package com.imad.project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
public class StudentController {

    @PostMapping("/single-student")
    public ResponseEntity<String> getStudent() {
        return ResponseEntity.ok("User logged successfully");
    }

}
