package com.imad.project.controller;

import com.imad.project.controller.dto.product.ProductRequestDto;
import com.imad.project.controller.dto.product.ProductDto;
import com.imad.project.controller.dto.product.ProductUpdateRequestDto;
import com.imad.project.mapper.UserMapper;
import com.imad.project.model.Product;
import com.imad.project.service.ICurrentUserService;
import com.imad.project.service.IProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final IProductService productService;
    private final ICurrentUserService currentUserService;

    public ProductController(IProductService productService, ICurrentUserService currentUserService) {
        this.productService = productService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> listProducts(@RequestParam(required = false) String category) {
        UUID requesterId = currentUserService.getUserId(); // lance 401 si non auth
        var products = productService.listProducts(requesterId, category);
        return ResponseEntity.ok(products.stream().map(UserMapper::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable UUID id) {
        UUID requesterId = currentUserService.getUserId();
        Product product = productService.getProduct(requesterId, id);
        return ResponseEntity.ok(UserMapper.toDto(product));
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductRequestDto req) {
        UUID requesterId = currentUserService.getUserId();
        Product created = productService.createProduct(requesterId, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toDto(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable UUID id, @RequestBody ProductUpdateRequestDto req) {
        UUID requesterId = currentUserService.getUserId();
        Product updated = productService.updateProduct(requesterId, id, req);
        return ResponseEntity.ok(UserMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        UUID requesterId = currentUserService.getUserId();
        productService.deleteProduct(requesterId, id);
        return ResponseEntity.noContent().build();
    }

}