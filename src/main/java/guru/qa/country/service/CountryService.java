package guru.qa.country.service;

import guru.qa.country.domain.Country;
import guru.qa.country.domain.graphql.CountryGql;
import guru.qa.country.domain.graphql.CountryInputGql;
import guru.qa.country.domain.grpc.CountryGrpc;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface CountryService {

    List<Country> allCountries();

    List<CountryGrpc> allGrpcCountries();

    Slice<CountryGql> allGqlCountries(Pageable pageable);

    Country countryByIso(String iso);

    CountryGrpc countryGrpcByIso(String iso);

    CountryGql countryGqlByIso(String iso);

    Country addCountry(Country country);

    CountryGrpc addCountry(CountryGrpc country);

    CountryGql addGqlCountry(CountryInputGql country);

    Country updateCountry(String iso, Country country);

    CountryGrpc updateCountryGrpc(String iso, List<List<List<List<Double>>>> coordinates);

    CountryGql updateGqlCountry(String iso, CountryInputGql country);
}
