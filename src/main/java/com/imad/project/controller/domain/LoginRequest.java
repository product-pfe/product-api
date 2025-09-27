package com.imad.project.controller.domain;

public record LoginRequest (
        String email,
        String password
) {
}
