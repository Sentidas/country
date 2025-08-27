package ru.sentidas.country.service;

import ru.sentidas.country.domain.Country;
import ru.sentidas.country.domain.CreateCountryInput;
import ru.sentidas.country.domain.UpdateCountryInput;

import java.util.List;

public interface CountryService {

    Country getById(String id);

    Country getByName(String name);

    List<Country> getAll();

    Country add(CreateCountryInput country);

    Country update(String id, UpdateCountryInput country);
}
