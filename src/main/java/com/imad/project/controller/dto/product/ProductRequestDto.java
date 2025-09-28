package com.imad.project.controller.dto.product;

import java.math.BigDecimal;
import java.util.List;

public record ProductRequestDto(
    String name,
    String description,
    BigDecimal price,
    String currency,
    String category,
    List<String> imageUrls,
    int quantity
) {}