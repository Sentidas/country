package ru.sentidas.country.controller.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sentidas.country.domain.CountryJson;
import ru.sentidas.country.service.CountryService;

@RestController
@RequestMapping("api/country/v2")
public class CountryV2Controller {

    private final CountryService countryService;

    @Autowired
    public CountryV2Controller(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/all")
    public Page<CountryJson> all(@PageableDefault Pageable pageable,
                                 @RequestParam(required = false) String searchQuery) {
        return countryService.getAll(pageable, searchQuery);
    }
}

