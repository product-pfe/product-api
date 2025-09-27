package com.imad.project.service;

import com.imad.project.controller.domain.Address;
import com.imad.project.controller.domain.Gender;
import com.imad.project.model.Role;
import com.imad.project.model.User;
import com.imad.project.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
public class AdminChangeLog implements CommandLineRunner {
    @Autowired
    private IUserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Address address = new Address(
                "Fes", "12", "123", "Maroc", "Fez"
        );
        User admin = new User(
                UUID.randomUUID(),
                "admin", "", "admin@gmail.com", address, LocalDate.of(1999,2,10), Gender.MALE, passwordEncoder.encode("1234"), Role.ADMIN
        );
        if (userRepository.findByEmail(admin.getEmail()).isEmpty()){
            userRepository.save(admin);
        }
    }
}
