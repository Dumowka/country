package guru.qa.country.service;

import guru.qa.country.data.CountryEntity;
import guru.qa.country.data.CountryRepository;
import guru.qa.country.domain.Country;
import guru.qa.country.ex.CountryNotFoundException;
import guru.qa.country.ex.CountryWithIsoAlreadyExist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
                .orElseThrow(() -> new CountryNotFoundException("Не найдена страна с ISO' = " + iso + "'"));
        return fromEntity(countryEntity);
    }

    @Transactional
    @Override
    public Country addCountry(Country country) {
        Optional<CountryEntity> countryEntityByIso = countryRepository.findCountryEntityByIso(country.iso());
        if (countryEntityByIso.isPresent()) {
            throw new CountryWithIsoAlreadyExist(String.format("Страна с ISO = '%s' уже существует", country.iso()));
        }

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
                .orElseThrow(() -> new CountryNotFoundException("Не найдена страна с ISO' = " + iso + "'"));

        countryEntity.setCoordinates(country.coordinates().toString());
        return fromEntity(countryRepository.save(countryEntity));
    }
}
