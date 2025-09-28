package com.imad.project.model;

import java.math.BigDecimal;
import java.util.List;

public record ProductRequest(
    String name,
    String description,
    BigDecimal price,
    String currency,
    Category category,
    List<String> imageUrls,
    Integer quantity,
    ProductStatus status
) {}