package ru.sentidas.country.service;

import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;
import ru.sentidas.country.domain.Country;
import ru.sentidas.country.domain.CreateCountryInput;
import ru.sentidas.country.domain.UpdateCountryInput;
import ru.sentidas.grpc.country.*;

import java.util.List;
import java.util.Random;

@Service
public class GrpcCountryService extends CountryServiceGrpc.CountryServiceImplBase {

    private final CountryService countryService;
    private final Random random = new Random();

    public GrpcCountryService(CountryService countryService) {
        this.countryService = countryService;
    }


    @Override
    public void country(IdRequest request, StreamObserver<CountryResponse> responseObserver) {
        final Country country = countryService.getById(request.getId());
        responseObserver.onNext(
                CountryResponse.newBuilder()
                        .setId(country.id().toString())
                        .setName(country.name())
                        .setCode(country.code())
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void addCountry(CountryRequest request, StreamObserver<CountryResponse> responseObserver) {
        final Country country = countryService.add(new CreateCountryInput(
                request.getName(), request.getCode()
        ));
        responseObserver.onNext(
                CountryResponse.newBuilder()
                        .setId(country.id().toString())
                        .setName(country.name())
                        .setCode(country.code())
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void updateCountry(UpdateCountryRequest request, StreamObserver<CountryResponse> responseObserver) {
        final Country country = countryService.update(
                request.getId(),
                new UpdateCountryInput(
                        request.getName(),
                        request.getCode()
                ));

        responseObserver.onNext(
                CountryResponse.newBuilder()
                        .setId(country.id().toString())
                        .setName(country.name())
                        .setCode(country.code())
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void randomCountries(CountRequest request, StreamObserver<CountryResponse> responseObserver) {
        List<Country> countries = countryService.getAll();
        for (int i = 0; i < request.getCount(); i++) {
            Country randomCountry = countries.get(random.nextInt(countries.size()));
            responseObserver.onNext(
                    CountryResponse.newBuilder()
                            .setId(randomCountry.id().toString())
                            .setName(randomCountry.name())
                            .setCode(randomCountry.code())
                            .build()
            );
        }
        responseObserver.onCompleted();
    }
}

