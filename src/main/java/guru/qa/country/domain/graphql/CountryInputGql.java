package guru.qa.country.domain.graphql;

import java.util.List;
import java.util.UUID;

public record CountryInputGql(
        String name,
        String iso,
        List<List<List<List<Double>>>> coordinates
) {
}
