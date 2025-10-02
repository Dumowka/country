package guru.qa.country.domain.grpc;

import guru.qa.country.data.CountryEntity;
import guru.qa.country.domain.Country;

import java.util.List;
import java.util.UUID;

import static guru.qa.country.utils.CoordinatesUtils.parseCoordinates;

public record CountryGrpc(
        UUID id,
        String name,
        String iso,
        List<List<List<List<Double>>>> coordinates
) {
    public static CountryGrpc fromEntity(CountryEntity countryEntity) {
        return new CountryGrpc(
                countryEntity.getId(),
                countryEntity.getName(),
                countryEntity.getIso(),
                parseCoordinates(countryEntity.getCoordinates())
        );
    }
}
