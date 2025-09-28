package com.imad.project.service;

import com.imad.project.controller.dto.product.ProductRequestDto;
import com.imad.project.controller.dto.product.ProductStatusDto;
import com.imad.project.controller.dto.product.ProductUpdateRequestDto;
import com.imad.project.exception.ProductException;
import com.imad.project.mapper.UserMapper;
import com.imad.project.model.Product;
import com.imad.project.model.Role;
import com.imad.project.model.User;
import com.imad.project.repository.IProductRepository;
import com.imad.project.repository.IUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService implements IProductService {

    private final IProductRepository productRepository;
    private final IUserRepository userRepository;

    public ProductService(IProductRepository productRepository, IUserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    /**
     * Liste des produits selon le rôle de requester (ADMIN ou USER).
     */
    @Override
    public List<Product> listProducts(UUID requesterId, String category) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new ProductException(HttpStatus.UNAUTHORIZED, "User not found"));

        boolean isAdmin = requester.getRole() == Role.ADMIN;

        if (isAdmin) {
            if (category == null || category.isBlank()) {
                return new ArrayList<>(productRepository.findAll());
            } else {
                return productRepository.findAllByCategory(category);
            }
        }

        // Non-admin: doit être USER
        if (requester.getRole() != Role.USER) {
            throw new ProductException(HttpStatus.FORBIDDEN, "Forbidden: only USERS or ADMINS can list products");
        }

        if (category == null || category.isBlank()) {
            return productRepository.findAllByOwnerId(requesterId);
        } else {
            return productRepository.findAllByOwnerIdAndCategory(requesterId, category);
        }
    }

    /**
     * Récupérer un product : admin can access all, user only own products.
     */
    @Override
    public Product getProduct(UUID requesterId, UUID productId) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new ProductException(HttpStatus.UNAUTHORIZED, "User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(HttpStatus.NOT_FOUND, "Product not found"));

        if (requester.getRole() != Role.USER || requester.getRole() != Role.ADMIN) {
            throw new ProductException(HttpStatus.FORBIDDEN, "Forbidden");
        }

        if (!product.ownerId().equals(requesterId)) {
            throw new ProductException(HttpStatus.FORBIDDEN, "Forbidden: you don't own this product");
        }
        return product;
    }

    /**
     * Création : l'utilisateur courant devient owner. ADMIN et USER peuvent créer (si tu veux limiter -> change la condition).
     */
    @Override
    public Product createProduct(UUID userId, ProductRequestDto product) {
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new ProductException(HttpStatus.UNAUTHORIZED, "User not found"));

        // Ex : on autorise USERS et ADMINS à créer un produit
        if (requester.getRole() != Role.USER && requester.getRole() != Role.ADMIN) {
            throw new ProductException(HttpStatus.FORBIDDEN, "Forbidden: cannot create products with this role");
        }
        var productToSave = UserMapper.toDomain(product, userId);
        return productRepository.save(productToSave);
    }

    @Override
    public Product updateProduct(UUID requesterId, UUID productId, ProductUpdateRequestDto req) {
        return null;
    }

    /**
     * Delete : admin can delete any product; user only their own.
     */
    @Override
    public void deleteProduct(UUID requesterId, UUID productId) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new ProductException(HttpStatus.UNAUTHORIZED, "User not found"));

        Product existing = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(HttpStatus.NOT_FOUND, "Product not found"));

        boolean isAdmin = requester.getRole() == Role.ADMIN;

        if (!isAdmin) {
            if (requester.getRole() != Role.USER) {
                throw new ProductException(HttpStatus.FORBIDDEN, "Forbidden");
            }
            if (!existing.ownerId().equals(requesterId)) {
                throw new ProductException(HttpStatus.FORBIDDEN, "Forbidden: you don't own this product");
            }
        }

        productRepository.deleteById(productId);
    }

    // getStats left as before (implement later)
    //@Override
    public ProductStatusDto getStats(UUID requesterId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}