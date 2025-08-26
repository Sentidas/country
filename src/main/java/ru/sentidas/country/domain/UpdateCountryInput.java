package ru.sentidas.country.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public record UpdateCountryInput(
        @JsonProperty("name")
        @Size(max = 50, message = "Name country can`t be longer than 50 characters")
        String name,
        @JsonProperty("code")
        @Pattern(regexp = "[A-Z]{2}", message = "Code must be exactly 2 uppercase letters")
        String code
) {
}
