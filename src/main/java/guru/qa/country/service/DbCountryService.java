package guru.qa.country.service;

import guru.qa.country.data.CountryEntity;
import guru.qa.country.data.CountryRepository;
import guru.qa.country.domain.Country;
import guru.qa.country.domain.graphql.CountryGql;
import guru.qa.country.domain.graphql.CountryInputGql;
import guru.qa.country.ex.CountryNotFoundException;
import guru.qa.country.ex.CountryWithIsoAlreadyExist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
    public Slice<CountryGql> allGqlCountries(Pageable pageable) {
        return countryRepository.findAll(pageable)
                .map(countryEntity -> new CountryGql(
                        countryEntity.getId(),
                        countryEntity.getName(),
                        countryEntity.getIso(),
                        parseCoordinates(countryEntity.getCoordinates())
                ));
    }

    @Override
    public Country countryByIso(String iso) {
        CountryEntity countryEntity = countryRepository.findCountryEntityByIso(iso)
                .orElseThrow(() -> new CountryNotFoundException("Не найдена страна с ISO' = " + iso + "'"));
        return fromEntity(countryEntity);
    }

    @Override
    public CountryGql countryGqlByIso(String iso) {
        return countryRepository.findCountryEntityByIso(iso)
                .map(countryEntity -> new CountryGql(
                        countryEntity.getId(),
                        countryEntity.getName(),
                        countryEntity.getIso(),
                        parseCoordinates(countryEntity.getCoordinates())
                )).orElseThrow(() -> new CountryNotFoundException("Не найдена страна с ISO' = " + iso + "'"));
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

    @Override
    public CountryGql addGqlCountry(CountryInputGql country) {
        Optional<CountryEntity> countryEntityByIso = countryRepository.findCountryEntityByIso(country.iso());
        if (countryEntityByIso.isPresent()) {
            throw new CountryWithIsoAlreadyExist(String.format("Страна с ISO = '%s' уже существует", country.iso()));
        }

        CountryEntity countryEntity = new CountryEntity();
        countryEntity.setName(country.name());
        countryEntity.setIso(country.iso());
        countryEntity.setCoordinates(country.coordinates().toString());

        CountryEntity savedCountryEntity = countryRepository.save(countryEntity);

        return new CountryGql(
                savedCountryEntity.getId(),
                savedCountryEntity.getName(),
                savedCountryEntity.getIso(),
                parseCoordinates(savedCountryEntity.getCoordinates())
        );
    }

    @Transactional
    @Override
    public Country updateCountry(String iso, Country country) {
        CountryEntity countryEntity = countryRepository.findCountryEntityByIso(iso)
                .orElseThrow(() -> new CountryNotFoundException("Не найдена страна с ISO' = " + iso + "'"));

        countryEntity.setCoordinates(country.coordinates().toString());
        return fromEntity(countryRepository.save(countryEntity));
    }

    @Override
    public CountryGql updateGqlCountry(String iso, CountryInputGql country) {
        CountryEntity countryEntity = countryRepository.findCountryEntityByIso(iso)
                .orElseThrow(() -> new CountryNotFoundException("Не найдена страна с ISO' = " + iso + "'"));

        countryEntity.setCoordinates(country.coordinates().toString());

        CountryEntity savedCountryEntity = countryRepository.save(countryEntity);

        return new CountryGql(
                savedCountryEntity.getId(),
                savedCountryEntity.getName(),
                savedCountryEntity.getIso(),
                parseCoordinates(savedCountryEntity.getCoordinates())
        );
    }
}
