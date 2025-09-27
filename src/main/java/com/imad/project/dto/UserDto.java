package com.imad.project.dto;

import com.imad.project.controller.domain.Address;
import com.imad.project.controller.domain.Gender;
import com.imad.project.model.UserStatus;

import java.util.UUID;

public record UserDto(
        UUID id,
        String firstName,
        String lastName,
        String email,
        Gender gender,
        UserStatus UserStatus
) {
}
