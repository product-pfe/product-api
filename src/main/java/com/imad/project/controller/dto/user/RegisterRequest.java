package com.imad.project.controller.dto.user;

import java.time.LocalDate;

public record RegisterRequest (
        String firstName,
        String lastName,
        String email,
        Address address,
        LocalDate dateOfBirth,
        Gender gender,
        String password,
        String confirmPassword
){}
