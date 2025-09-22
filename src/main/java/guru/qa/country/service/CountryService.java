package guru.qa.country.service;

import guru.qa.country.domain.Country;
import guru.qa.country.domain.graphql.CountryGql;
import guru.qa.country.domain.graphql.CountryInputGql;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface CountryService {

    List<Country> allCountries();

    Slice<CountryGql> allGqlCountries(Pageable pageable);

    Country countryByIso(String iso);

    CountryGql countryGqlByIso(String iso);

    Country addCountry(Country country);

    CountryGql addGqlCountry(CountryInputGql country);

    Country updateCountry(String iso, Country country);

    CountryGql updateGqlCountry(String iso, CountryInputGql country);
}
