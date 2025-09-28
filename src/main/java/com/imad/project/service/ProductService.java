package com.imad.project.service;

import com.imad.project.controller.dto.product.ProductRequestDto;
import com.imad.project.controller.dto.product.ProductStatusDto;
import com.imad.project.controller.dto.product.ProductUpdateRequestDto;
import com.imad.project.exception.ProductException;
import com.imad.project.mapper.UserMapper;
import com.imad.project.model.Product;
import com.imad.project.model.ProductStatus;
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
        boolean hasCategory = category != null && !category.isBlank();

        // 1) Requête anonyme / publique
        if (requesterId == null) {
            // OPTIONNEL : ne renvoyer que les produits ACTIVE pour les visiteurs
            if (hasCategory) {
                return productRepository.findAllByCategoryAndStatus(category, ProductStatus.ACTIVE);
            } else {
                return productRepository.findAllByStatus(ProductStatus.ACTIVE);
            }
        }

        // 2) Utilisateur authentifié — charger pour vérifier le rôle
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new ProductException(HttpStatus.UNAUTHORIZED, "User not found"));

        // 3) Admin -> voit tout
        if (requester.getRole() == Role.ADMIN) {
            if (hasCategory) {
                return productRepository.findAllByCategory(category);
            } else {
                return productRepository.findAll();
            }
        }

        // 4) User -> voit seulement ses produits
        if (requester.getRole() == Role.USER) {
            if (hasCategory) {
                return productRepository.findAllByOwnerIdAndCategory(requesterId, category);
            } else {
                return productRepository.findAllByOwnerId(requesterId);
            }
        }

        // 5) Autres rôles non autorisés
        throw new ProductException(HttpStatus.FORBIDDEN, "Forbidden: role not allowed to list products");
    }

    /**
     * Récupérer un product : admin can access all, user only own products.
     */
    @Override
    public Product getProduct(UUID productId) {
        return productRepository.findById(productId)
               .orElseThrow(() -> new ProductException(HttpStatus.NOT_FOUND, "Product not found"));
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