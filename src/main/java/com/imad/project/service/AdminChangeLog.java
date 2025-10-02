package com.imad.project.service;

import com.imad.project.controller.dto.user.Address;
import com.imad.project.controller.dto.user.Gender;
import com.imad.project.model.Role;
import com.imad.project.model.User;
import com.imad.project.model.Status;
import com.imad.project.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
@Order(1)
public class AdminChangeLog implements CommandLineRunner {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminChangeLog(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // adresse exemple (tu peux la réutiliser ou en créer plusieurs)
        Address address = new Address("Fes", "12", "123", "Maroc", "Fez");

        // --- Admin (déjà présent dans ta version) ---
        User admin = new User(
                UUID.randomUUID(),
                "admin",
                "",
                "admin@gmail.com",
                address,
                LocalDate.of(1999, 2, 10),
                Gender.MALE,
                passwordEncoder.encode("Test!123"),
                Status.ACCEPTED,
                Role.ADMIN
        );

        if (userRepository.findByEmail(admin.getEmail()).isEmpty()) {
            userRepository.save(admin);
            System.out.println("Admin created: " + admin.getEmail());
        } else {
            System.out.println("Admin already exists: " + admin.getEmail());
        }

        // --- Utilisateurs à créer ---
        String plain = "Test!123";
        List<User> usersToCreate = List.of(
                new User(UUID.randomUUID(), "Alice", "Dupont", "alice@example.com", address,
                        LocalDate.of(1995, 5, 12), Gender.FEMALE, passwordEncoder.encode(plain),
                        Status.ACCEPTED, Role.USER),
                new User(UUID.randomUUID(), "Bob", "Martin", "bob@example.com", address,
                        LocalDate.of(1990, 8, 3), Gender.MALE, passwordEncoder.encode(plain),
                        Status.ACCEPTED, Role.USER),
                new User(UUID.randomUUID(), "Carla", "Ben", "carla@example.com", address,
                        LocalDate.of(1998, 11, 21), Gender.FEMALE, passwordEncoder.encode(plain),
                        Status.ACCEPTED, Role.USER)
        );

        for (User u : usersToCreate) {
            if (userRepository.findByEmail(u.getEmail()).isEmpty()) {
                userRepository.save(u);
                System.out.println("User created: " + u.getEmail());
            } else {
                System.out.println("User already exists: " + u.getEmail());
            }
        }
    }
}
