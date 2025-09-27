package com.imad.project.controller.domain;

public record Address(
        String street,
        String number,
        String zipcode,
        String country,
        String city
) {
}
