package ru.sentidas.country.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sentidas.country.data.CountryEntity;
import ru.sentidas.country.data.CountryRepository;
import ru.sentidas.country.domain.Country;
import ru.sentidas.country.domain.CreateCountryInput;
import ru.sentidas.country.domain.UpdateCountryInput;
import ru.sentidas.country.ex.CountryNotFoundException;

import java.util.List;
import java.util.UUID;

@Component
public class DbCountryService implements CountryService {

    private final CountryRepository countryRepository;

    @Autowired
    public DbCountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public List<Country> getAll() {
        return countryRepository.findAll()
                .stream()
                .map(ce -> {
                    return new Country(
                            ce.getId(),
                            ce.getName(),
                            ce.getCode()
                    );
                }).toList();
    }

    @Override
    public Country getById(String id) {
        return countryRepository.findById(UUID.fromString(id))
                .map(ce -> new Country(
                            ce.getId(),
                            ce.getName(),
                            ce.getCode()
                    )).orElseThrow(CountryNotFoundException::new);
    }

    @Override
    public Country getByName(String name) {
        return countryRepository.findByName(name)
                .map(ce -> new Country(
                            ce.getId(),
                            ce.getName(),
                            ce.getCode()
                )).orElseThrow(CountryNotFoundException::new);
    }

    @Override
    public Country add(CreateCountryInput country) {
        CountryEntity countryEntity = new CountryEntity();
        countryEntity.setName(country.name());
        countryEntity.setCode(country.code());

        CountryEntity savedCountry = countryRepository.save(countryEntity);

        return new Country(savedCountry.getId(), savedCountry.getName(), savedCountry.getCode());
    }

    @Override
    public Country update(String id, UpdateCountryInput country) {

        CountryEntity countryEntity = countryRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("Country not found with id: '" + id + "'"));

        if (country.code() != null) {
            countryEntity.setCode(country.code());
        }

        if (country.name() != null) {
            countryEntity.setName(country.name());
        }
        CountryEntity updatedCountry = countryRepository.save(countryEntity);

        return new Country(updatedCountry.getId(), updatedCountry.getName(), updatedCountry.getCode());
    }
}
