package com.imad.project.dto;

import com.imad.project.controller.domain.Gender;
import com.imad.project.model.Status;

import java.util.UUID;

public record UserDto(
        UUID id,
        String firstName,
        String lastName,
        String email,
        Gender gender,
        Status Status
) {
}
