package ru.sentidas.country.data;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sentidas.country.domain.Country;

import java.util.Optional;
import java.util.UUID;

public interface CountryRepository extends JpaRepository<CountryEntity, UUID> {

    Optional<CountryEntity> findByName(String name);
}
