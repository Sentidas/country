package ru.sentidas.country.service;

import ru.sentidas.country.domain.Country;

import java.util.List;

public interface CountryService {

    List<Country> allCountries();

    Country addCountry(Country country);

    Country editCountry(String code, String editName);

}
