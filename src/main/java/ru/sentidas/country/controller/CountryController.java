package ru.sentidas.country.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sentidas.country.domain.Country;
import ru.sentidas.country.domain.CountryError;
import ru.sentidas.country.domain.CountryNameUpdateRequest;
import ru.sentidas.country.service.CountryService;

import java.util.List;

@RestController
@RequestMapping("api/country")
public class CountryController {

    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/all")
    public List<Country> all() {
        return countryService.allCountries();
    }

    @PostMapping("/add")
    public Country add(@RequestBody Country country) {
        return countryService.addCountry(country);
    }


    @PatchMapping("/edit/{code}")
    public ResponseEntity<?> edit(
            @PathVariable String code,
            @RequestBody CountryNameUpdateRequest request) {

        if (!code.matches("[A-Z]{2}")) {
            return ResponseEntity.badRequest()
                    .body(new CountryError("INVALID_CODE", "Country code must be exactly 2 uppercase letters"));
        }

        if (request.updatedName() == null || request.updatedName().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new CountryError("EMPTY_NAME", "Country name cannot be empty"));
        }
        try {
            Country country = countryService.editCountry(code, request.updatedName());
            return ResponseEntity.ok(country);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CountryError("CODE_COUNTRY_NOT_FOUND", e.getMessage()));
        }
    }
}
