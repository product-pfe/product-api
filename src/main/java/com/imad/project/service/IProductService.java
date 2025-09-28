package com.imad.project.service;

import com.imad.project.controller.dto.product.ProductRequestDto;
import com.imad.project.controller.dto.product.ProductUpdateRequestDto;
import com.imad.project.model.Product;

import java.util.List;
import java.util.UUID;

public interface IProductService {
    List<Product> listProducts(UUID requesterId, String category);

    Product getProduct(UUID requesterId, UUID productId);

    Product createProduct(UUID requesterId, ProductRequestDto req);

    Product updateProduct(UUID requesterId, UUID productId, ProductUpdateRequestDto req);
    void deleteProduct(UUID requesterId, UUID productId);
    //ProductStatsDto getStats(UUID requesterId);
}
