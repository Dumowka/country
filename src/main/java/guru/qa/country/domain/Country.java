package guru.qa.country.domain;

import guru.qa.country.data.CountryEntity;

import java.util.List;

import static guru.qa.country.utils.CoordinatesUtils.parseCoordinates;

public record Country(
        String name,
        String iso,
        List<List<List<List<Double>>>> coordinates
) {
    public static Country fromEntity(CountryEntity countryEntity) {
        return new Country(
                countryEntity.getName(),
                countryEntity.getIso(),
                parseCoordinates(countryEntity.getCoordinates())
        );
    }
}
