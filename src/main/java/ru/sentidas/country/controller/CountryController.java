package ru.sentidas.country.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.sentidas.country.domain.Country;
import ru.sentidas.country.domain.CreateCountryInput;
import ru.sentidas.country.domain.UpdateCountryInput;
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
        return countryService.getAll();
    }

    @PostMapping("/add")
    public Country add(@Valid @RequestBody CreateCountryInput country) {
        return countryService.add(country);
    }

    @PatchMapping("/update/{id}")
    public Country update(@PathVariable String id,
                          @Valid @RequestBody UpdateCountryInput country) {
        return countryService.update(id, country);
    }

    @GetMapping("/{id}")
    public Country byId(@PathVariable String id) {
        return countryService.getById(id);
    }

    @GetMapping("/search")
    public Country byName(@RequestParam("name") String name) {
        return countryService.getByName(name);
    }
}

