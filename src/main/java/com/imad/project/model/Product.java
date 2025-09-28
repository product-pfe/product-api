package com.imad.project.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Document("products")
public record Product (

    @Id
    UUID id,
    UUID ownerId,            // référence à User.id
    String name,
    String description,
    BigDecimal price,
    String currency,         // "EUR", "USD"
    Category category,         // ou enum Category
    List<String> imageUrls,  // stockées dans S3, Cloudinary, ...
    int quantity,
    ProductStatus status,
    Instant createdAt,
    Instant updatedAt
){}