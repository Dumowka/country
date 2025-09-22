package guru.qa.country.controller.graphql;

import guru.qa.country.domain.graphql.CountryGql;
import guru.qa.country.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class CountryQueryController {

    private final CountryService countryService;

    @Autowired
    public CountryQueryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @QueryMapping
    public Slice<CountryGql> countries(@Argument int page, @Argument int size) {
        return countryService.allGqlCountries(PageRequest.of(page, size));
    }

    @QueryMapping
    public CountryGql country(@Argument String iso) {
        return countryService.countryGqlByIso(iso);
    }
}
