package com.imad.project.controller.dto.product;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ProductDto(
    UUID id,
    UUID ownerId,
    String name,
    String description,
    BigDecimal price,
    String currency,
    CategoryDto category,
    List<String> imageUrls,
    int quantity,
    ProductStatusDto status,
    Instant createdAt,
    Instant updatedAt
) {}