package ru.sentidas.country.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record Country(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("name")
        String name,
        @JsonProperty("code")
        String code
) {
}
