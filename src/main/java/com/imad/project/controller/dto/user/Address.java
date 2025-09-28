package com.imad.project.controller.dto.user;

public record Address(
        String street,
        String number,
        String zipcode,
        String country,
        String city
) {
}
