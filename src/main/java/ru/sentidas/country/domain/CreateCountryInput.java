package ru.sentidas.country.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public record CreateCountryInput(
        @JsonProperty("name")
        @NotBlank(message = "Name is required")
        @Size(max = 50, message = "Name country can`t be longer than 50 characters")
        String name,
        @JsonProperty("code")
        @Pattern(regexp = "[A-Z]{2}", message = "Code must be exactly 2 uppercase letters")
        String code
) {
}
