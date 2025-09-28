package com.imad.project.controller.dto.user;

public record LoginRequest (
        String email,
        String password
) {
}
