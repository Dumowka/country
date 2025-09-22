package guru.qa.country.domain.graphql;

import guru.qa.country.domain.Country;

import java.util.List;
import java.util.UUID;

public record CountryGql(
        UUID id,
        String name,
        String iso,
        List<List<List<List<Double>>>> coordinates
) {
}
