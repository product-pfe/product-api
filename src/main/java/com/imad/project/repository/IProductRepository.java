package com.imad.project.repository;

import com.imad.project.model.Product;
import com.imad.project.model.ProductStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IProductRepository extends MongoRepository<Product, UUID> {

    List<Product> findAllByOwnerId(UUID ownerId);

    List<Product> findAllByOwnerIdAndCategory(UUID ownerId, String category);

    // pour admin
    List<Product> findAllByCategory(String category);
    List<Product> findAllByStatus(ProductStatus status);
    List<Product> findAllByCategoryAndStatus(String category, ProductStatus status);


    long countByOwnerId(UUID ownerId);

}