package com.imad.project.exception;

import java.time.Instant;
import java.util.Map;

public record ProductErrorId(
        int status,
        String error,
        String message,
        String path,
        Instant timestamp,
        Map<String, String> validationErrors
) {}