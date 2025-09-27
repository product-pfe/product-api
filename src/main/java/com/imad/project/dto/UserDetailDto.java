package com.imad.project.dto;

import com.imad.project.controller.domain.Address;
import com.imad.project.controller.domain.Gender;
import com.imad.project.model.UserStatus;

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
        UserStatus UserStatus
) {
}
