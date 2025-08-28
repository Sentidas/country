package ru.sentidas.country.service.soap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.sentidas.country.domain.CountryJson;
import ru.sentidas.country.domain.CreateCountryInput;
import ru.sentidas.country.domain.UpdateCountryInput;
import ru.sentidas.country.service.CountryService;
import ru.sentidas.xml.country.*;

import static ru.sentidas.country.config.AppConfig.SOAP_NAMESPACE;

@Endpoint
public class CountryEndpoint {

    private final CountryService countryService;

    @Autowired
    public CountryEndpoint(CountryService countryService) {
        this.countryService = countryService;
    }

    @PayloadRoot(namespace = SOAP_NAMESPACE, localPart = "idRequest")
    @ResponsePayload
    public CountryResponse countryById(@RequestPayload IdRequest request) {
        CountryJson country = countryService.getById(request.getId());
        CountryResponse response = new CountryResponse();
        ru.sentidas.xml.country.Country xmlCountry = new ru.sentidas.xml.country.Country();
        xmlCountry.setId(country.id().toString());
        xmlCountry.setCode(country.code());
        xmlCountry.setName(country.name());
        response.setCountry(xmlCountry);
        return response;
    }

    @PayloadRoot(namespace = SOAP_NAMESPACE, localPart = "nameRequest")
    @ResponsePayload
    public CountryResponse countryByName(@RequestPayload NameRequest request) {
        CountryJson country = countryService.getByName(request.getName());
        CountryResponse response = new CountryResponse();
        ru.sentidas.xml.country.Country xmlCountry = new ru.sentidas.xml.country.Country();
        xmlCountry.setId(country.id().toString());
        xmlCountry.setCode(country.code());
        xmlCountry.setName(country.name());
        response.setCountry(xmlCountry);
        return response;
    }

    @PayloadRoot(namespace = SOAP_NAMESPACE, localPart = "countryInputRequest")
    @ResponsePayload
    public CountryResponse add(@RequestPayload CountryInputRequest request) {
        CountryJson addedCountry = countryService.add(new CreateCountryInput(request.getName(), request.getCode()));

        CountryResponse response = new CountryResponse();
        Country xmlCountry = new Country();
        xmlCountry.setId(addedCountry.id().toString());
        xmlCountry.setCode(addedCountry.code());
        xmlCountry.setName(addedCountry.name());
        response.setCountry(xmlCountry);
        return response;
    }

    @PayloadRoot(namespace = SOAP_NAMESPACE, localPart = "pageRequest")
    @ResponsePayload
    public CountriesPageResponse countries(@RequestPayload PageRequest request) {

        int page = request.getPage() > 0 ? request.getPage() : 0;
        int size = request.getSize() > 0 ? request.getSize() : 5;

        String search = (request.getSearchQuery().isBlank()) ? null : request.getSearchQuery();


        Page<CountryJson> pageRequest = countryService.getAll(
                org.springframework.data.domain.PageRequest.of(page, size),
                search
        );

        CountriesPageResponse response = new CountriesPageResponse();
        response.setTotalElements(pageRequest.getTotalElements());
        response.setTotalPages(pageRequest.getTotalPages());
        response.setPageNumber(pageRequest.getNumber());
        response.setPageSize(pageRequest.getSize());
        response.getCountries().addAll(
                pageRequest.getContent().stream().map(
                        countryJson -> {
                            Country xmlCountry = new Country();
                            xmlCountry.setId(countryJson.id().toString());
                            xmlCountry.setName(countryJson.name());
                            xmlCountry.setCode(countryJson.code());
                            return xmlCountry;
                        }
                ).toList()
        );
        return response;
    }

    @PayloadRoot(namespace = SOAP_NAMESPACE, localPart = "countryUpdateRequest")
    @ResponsePayload
    public CountryResponse update(@RequestPayload CountryUpdateRequest request) {
        CountryJson updateCountry = countryService.update(request.getId(),
                new UpdateCountryInput(request.getName(), request.getCode()));

        CountryResponse response = new CountryResponse();
        Country xmlCountry = new Country();
        xmlCountry.setId(updateCountry.id().toString());
        xmlCountry.setCode(updateCountry.code());
        xmlCountry.setName(updateCountry.name());
        response.setCountry(xmlCountry);
        return response;
    }

    @PayloadRoot(namespace = SOAP_NAMESPACE, localPart = "countryDeleteRequest")
    @ResponsePayload
    public DeleteCountryResponse delete(@RequestPayload CountryDeleteRequest request) {
        countryService.deleteById(request.getId());

        DeleteCountryResponse response = new DeleteCountryResponse();
        response.setSuccess(true);
        response.setMessage("Deleted country with id: " + request.getId());

        return response;
    }
}
