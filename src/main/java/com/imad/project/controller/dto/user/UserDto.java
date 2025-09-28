package com.imad.project.controller.dto.user;

import com.imad.project.model.Status;

import java.util.UUID;

public record UserDto(
        UUID id,
        String firstName,
        String lastName,
        String email,
        Gender gender,
        Status status
) {
}
