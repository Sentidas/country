package ru.sentidas.country.domain;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record CountryJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("name")
        String name,
        @JsonProperty("code")
        String code,
        @JsonProperty("last_modify_date")
        Date lastModifyDate
) {
}
