package ru.sentidas.country.service;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.sentidas.country.domain.CountryJson;
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
    public void countryById(IdRequest request, StreamObserver<CountryResponse> responseObserver) {
        final CountryJson country = countryService.getById(request.getId());
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
    public void countryByName(NameRequest request, StreamObserver<CountryResponse> responseObserver) {
        final CountryJson country = countryService.getByName(request.getName());
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
    public void searchCountries(NameRequest request, StreamObserver<CountriesResponse> responseObserver) {
        final List<CountryJson> countries = countryService.getByNames(request.getName());

        CountriesResponse.Builder responseBuilder = CountriesResponse.newBuilder();
        for (CountryJson c : countries) {
            responseBuilder.addCountries(
                    CountryResponse.newBuilder()
                            .setId(c.id().toString())
                            .setName(c.name())
                            .setCode(c.code())
                            .build()
            );
        }
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void allCountries(com.google.protobuf.Empty request, StreamObserver<CountriesResponse> responseObserver) {
        final List<CountryJson> countries = countryService.getAll();

        CountriesResponse.Builder responseBuilder = CountriesResponse.newBuilder();
        for(CountryJson c : countries) {
            responseBuilder.addCountries(
                    CountryResponse.newBuilder()
                            .setId(c.id().toString())
                            .setName(c.name())
                            .setCode(c.code())
                            .build()
            );
        }
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void countriesPaged(ru.sentidas.grpc.country.PageRequest request,
                               io.grpc.stub.StreamObserver<ru.sentidas.grpc.country.PagedCountries> responseObserver) {

        int page = request.getPage() > 0 ? request.getPage() : 0;
        int size = request.getSize() > 0 ? request.getSize() : 12;

        Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);

        String search = request.getSearchQuery().isBlank() ? null : request.getSearchQuery();

        Page<CountryJson> result = countryService.getAll(pageable, search);

        PagedCountries.Builder resp = PagedCountries.newBuilder()
                .setTotalElements(result.getTotalElements())
                .setTotalPages(result.getTotalPages())
                .setPageNumber(result.getNumber())
                .setPageSize(result.getSize());

        result.forEach(c -> resp.addCountries(
                CountryResponse.newBuilder()
                        .setId(c.id().toString())
                        .setName(c.name())
                        .setCode(c.code())
                        .build()
        ));

        responseObserver.onNext(resp.build());
        responseObserver.onCompleted();
    }

    @Override
    public void addCountry(CountryRequest request, StreamObserver<CountryResponse> responseObserver) {
        final CountryJson country = countryService.add(new CreateCountryInput(
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
        final CountryJson country = countryService.update(
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
    public void deleteCountry(IdRequest request, StreamObserver<Empty> responseObserver) {
        countryService.deleteById(request.getId());
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void randomCountries(CountRequest request, StreamObserver<CountryResponse> responseObserver) {
        List<CountryJson> countries = countryService.getAll();
        for (int i = 0; i < request.getCount(); i++) {
            CountryJson randomCountry = countries.get(random.nextInt(countries.size()));
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

