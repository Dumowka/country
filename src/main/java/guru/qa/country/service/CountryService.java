package guru.qa.country.service;

import guru.qa.country.domain.Country;

import java.util.List;

public interface CountryService {

    List<Country> allCountries();

    Country countryByIso(String iso);

    Country addCountry(Country country);

    Country updateCountry(String iso, Country country);
}
