package com.imad.project.controller.dto.user;

import com.imad.project.model.Status;

import java.time.LocalDate;
import java.util.UUID;

public record UserDetailDto(
        UUID id,
        String firstName,
        String lastName,
        String email,
        Address address,
        LocalDate dateOfBirth,
        Gender gender,
        Status Status
) {
}
