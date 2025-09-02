package ru.sentidas.country.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ru.sentidas.country.domain.CountryJson;
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
    public List<CountryJson> all() {
        return countryService.getAll();
    }

    @PostMapping("/add")
    public CountryJson add(@Valid @RequestBody CreateCountryInput country) {
        return countryService.add(country);
    }

    @PatchMapping("/update/{id}")
    public CountryJson update(@PathVariable String id,
                              @Valid @RequestBody UpdateCountryInput country) {
        return countryService.update(id, country);
    }

    @GetMapping("/{id}")
    public CountryJson byId(@PathVariable String id) {
        return countryService.getById(id);
    }

    @GetMapping("/search")
    public CountryJson byName(@RequestParam("name") String name) {
        return countryService.getByName(name);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        countryService.deleteById(id);
    }


}

