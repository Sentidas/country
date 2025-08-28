package ru.sentidas.country.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.sentidas.country.domain.CountryJson;
import ru.sentidas.country.domain.CreateCountryInput;
import ru.sentidas.country.domain.UpdateCountryInput;

import javax.annotation.Nullable;
import java.util.List;

public interface CountryService {

    CountryJson getById(String id);

    CountryJson getByName(String name);

    List<CountryJson> getByNames(String name);

    List<CountryJson> getAll();

    Page<CountryJson> getAll(Pageable pageable, @Nullable String searchQuery);

    CountryJson add(CreateCountryInput country);

    CountryJson update(String id, UpdateCountryInput country);

    void deleteById(String id);
}
