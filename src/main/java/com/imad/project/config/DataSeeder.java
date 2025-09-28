package com.imad.project.config;

import com.imad.project.model.Category;
import com.imad.project.model.Product;
import com.imad.project.model.ProductStatus;
import com.imad.project.model.User;
import com.imad.project.repository.IProductRepository;
import com.imad.project.repository.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Configuration
public class DataSeeder {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);
    private static final Random rnd = new Random();

    @Bean
    public CommandLineRunner seedProducts(IProductRepository productRepository, IUserRepository userRepository) {
        return args -> {
            final int TARGET = 200; // <-- target changed to 200
            long existingCount = productRepository.count();

            if (existingCount >= TARGET) {
                log.info("Product seeding skipped: {} products already present (target {}).", existingCount, TARGET);
                return;
            }

            int toCreate = (int) (TARGET - existingCount);
            log.info("Seeding products: existing={}, target={}, will create {} products.", existingCount, TARGET, toCreate);

            List<User> users = userRepository.findAll();
            if (users.isEmpty()) {
                log.warn("No users found in DB — cannot assign owners to seeded products. Skipping product seeding.");
                return;
            }

            String[] adjectives = {"Super", "Pro", "Ultra", "Mini", "Max", "Eco", "Smart", "Classic", "Deluxe", "Compact"};
            String[] nouns = {"Phone", "Book", "Watch", "Headphones", "Backpack", "Lamp", "Chair", "Jacket", "Sneakers", "Mug"};
            Category[] categories = Category.values();

            List<Product> batch = new ArrayList<>(toCreate);
            for (int i = 0; i < toCreate; i++) {
                UUID id = UUID.randomUUID();
                UUID ownerId = users.get(rnd.nextInt(users.size())).getId();

                String name = adjectives[rnd.nextInt(adjectives.length)] + " " + nouns[rnd.nextInt(nouns.length)] + " " + (rnd.nextInt(9000) + 100);
                String description = "Description for " + name + " — produit seedé automatiquement.";
                BigDecimal price = BigDecimal.valueOf(5 + rnd.nextDouble() * 495)
                        .setScale(2, RoundingMode.HALF_UP);
                String currency = "EUR";
                Category category = categories[rnd.nextInt(categories.length)];
                List<String> images = List.of("https://picsum.photos/seed/" + id + "/600/400");
                int quantity = 1 + rnd.nextInt(50);
                Instant now = Instant.now();

                Product p = new Product(
                        id,
                        ownerId,
                        name,
                        description,
                        price,
                        currency,
                        category,
                        images,
                        quantity,
                        ProductStatus.ACTIVE,
                        now,
                        now
                );
                batch.add(p);
            }

            productRepository.saveAll(batch);
            log.info("Seeded {} products into database (was {}).", batch.size(), existingCount);
        };
    }
}