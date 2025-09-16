package guru.qa.country.service;

import guru.qa.country.data.CountryEntity;
import guru.qa.country.data.CountryRepository;
import guru.qa.country.domain.Country;
import guru.qa.country.ex.CountryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static guru.qa.country.domain.Country.fromEntity;
import static guru.qa.country.utils.CoordinatesUtils.parseCoordinates;

@Component
public class DbCountryService implements CountryService {

    private final CountryRepository countryRepository;

    @Autowired
    public DbCountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public List<Country> allCountries() {
        return countryRepository.findAll().stream().map(countryEntity ->
                new Country(
                        countryEntity.getName(),
                        countryEntity.getIso(),
                        parseCoordinates(countryEntity.getCoordinates())
                )
        ).toList();
    }

    @Override
    public Country countryByIso(String iso) {
        CountryEntity countryEntity = countryRepository.findCountryEntityByIso(iso)
                .orElseThrow(() -> new CountryNotFoundException("Не найдена страна с ISO'" + iso + "'"));
        return fromEntity(countryEntity);
    }

    @Transactional
    @Override
    public Country addCountry(Country country) {
        CountryEntity countryEntity = new CountryEntity();
        countryEntity.setName(country.name());
        countryEntity.setIso(country.iso());
        countryEntity.setCoordinates(country.coordinates().toString());

        return fromEntity(countryRepository.save(countryEntity));
    }

    @Transactional
    @Override
    public Country updateCountry(String iso, Country country) {
        CountryEntity countryEntity = countryRepository.findCountryEntityByIso(iso)
                .orElseThrow(() -> new CountryNotFoundException("Не найдена страна с ISO'" + iso + "'"));

        countryEntity.setCoordinates(country.coordinates().toString());
        return fromEntity(countryRepository.save(countryEntity));
    }
}
