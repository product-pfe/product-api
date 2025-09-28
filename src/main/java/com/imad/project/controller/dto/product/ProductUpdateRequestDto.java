package com.imad.project.controller.dto.product;

import com.imad.project.model.Category;

import java.math.BigDecimal;
import java.util.List;

public record ProductUpdateRequestDto(
    String name,
    String description,
    BigDecimal price,
    String currency,
    Category category,
    List<String> imageUrls,
    Integer quantity,
    ProductStatusDto status
) {}