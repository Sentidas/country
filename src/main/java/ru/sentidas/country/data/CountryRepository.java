package ru.sentidas.country.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CountryRepository extends JpaRepository<CountryEntity, UUID> {

    Optional<CountryEntity> findFirstByName(String name);

    List<CountryEntity> findAllByName(String name);

    Page<CountryEntity> findAllByName(String searchQuery, Pageable pageable);
}
