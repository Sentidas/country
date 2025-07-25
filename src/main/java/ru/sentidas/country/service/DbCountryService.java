package ru.sentidas.country.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sentidas.country.data.CountryEntity;
import ru.sentidas.country.data.CountryRepository;
import ru.sentidas.country.domain.Country;

import java.util.List;

@Component
public class DbCountryService implements CountryService {

    private final CountryRepository countryRepository;

    @Autowired
    public DbCountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public List<Country> allCountries() {
        return countryRepository.findAll()
                .stream()
                .map(ce -> {
                    return new Country(
                            ce.getName(),
                            ce.getCode()
                    );
                }).toList();
    }

    @Override
    public Country addCountry(Country country) {
        CountryEntity countryEntity = new CountryEntity();
        countryEntity.setName(country.name());
        countryEntity.setCode(country.code());

        CountryEntity savedCountry = countryRepository.save(countryEntity);

        return new Country(savedCountry.getName(), savedCountry.getCode());
    }

    @Override
    public Country editCountry(String code, String editName) {

        CountryEntity country = countryRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("County not found with code: " + code));

        country.setName(editName);
        CountryEntity undatedCountry = countryRepository.save(country);

        return new Country(undatedCountry.getName(), undatedCountry.getCode());
    }
}
