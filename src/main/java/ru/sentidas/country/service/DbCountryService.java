package ru.sentidas.country.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.sentidas.country.data.CountryEntity;
import ru.sentidas.country.data.CountryRepository;
import ru.sentidas.country.domain.CountryJson;
import ru.sentidas.country.domain.CreateCountryInput;
import ru.sentidas.country.domain.UpdateCountryInput;
import ru.sentidas.country.ex.CountryNotFoundException;

import javax.annotation.Nullable;
import java.util.Date;
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
    public List<CountryJson> getAll() {
        return countryRepository.findAll()
                .stream()
                .map(ce -> {
                    return new CountryJson(
                            ce.getId(),
                            ce.getName(),
                            ce.getCode(),
                            new Date()
                    );
                }).toList();
    }

    @Override
    public CountryJson getById(String id) {
        return countryRepository.findById(UUID.fromString(id))
                .map(ce -> new CountryJson(
                        ce.getId(),
                        ce.getName(),
                        ce.getCode(),
                        new Date()
                )).orElseThrow(CountryNotFoundException::new);
    }

    @Override
    public CountryJson getByName(String name) {
        return countryRepository.findFirstByName(name)
                .map(ce -> new CountryJson(
                        ce.getId(),
                        ce.getName(),
                        ce.getCode(),
                        new Date()
                )).orElseThrow(CountryNotFoundException::new);
    }

    @Override
    public List<CountryJson> getByNames(String name) {
        return countryRepository.findAllByName(name)
                .stream()
                .map(ce -> {
                    return new CountryJson(
                            ce.getId(),
                            ce.getName(),
                            ce.getCode(),
                            new Date()
                    );
                }).toList();
    }

    @Override
    public Page<CountryJson> getAll(Pageable pageable,
                                    @Nullable String searchQuery) {

        Page<CountryEntity> page;

        page = (searchQuery == null || searchQuery.isBlank())
                ? countryRepository.findAll(pageable)
                : countryRepository.findAllByName(searchQuery, pageable);

//        if (searchQuery == null || searchQuery.isBlank()) {
//            page = countryRepository.findAll(pageable);
//        } else {
//            page = countryRepository.findAllByName(searchQuery, pageable);
//        }

        return page.map(countryEntity -> new CountryJson(
                countryEntity.getId(),
                countryEntity.getName(),
                countryEntity.getCode(),
                new Date()
        ));
    }

    @Override
    public CountryJson add(CreateCountryInput country) {
        CountryEntity countryEntity = new CountryEntity();
        countryEntity.setName(country.name());
        countryEntity.setCode(country.code());

        CountryEntity savedCountry = countryRepository.save(countryEntity);

        return new CountryJson(savedCountry.getId(), savedCountry.getName(), savedCountry.getCode(), new Date());
    }

    @Override
    public CountryJson update(String id, UpdateCountryInput country) {

        CountryEntity countryEntity = countryRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("Country not found with id: '" + id + "'"));

        if (country.code() != null) {
            countryEntity.setCode(country.code());
        }

        if (country.name() != null) {
            countryEntity.setName(country.name());
        }
        CountryEntity updatedCountry = countryRepository.save(countryEntity);

        return new CountryJson(updatedCountry.getId(), updatedCountry.getName(), updatedCountry.getCode(), new Date());
    }

    @Override
    public void deleteById(String id) {
        countryRepository.deleteById(UUID.fromString(id));
    }
}
