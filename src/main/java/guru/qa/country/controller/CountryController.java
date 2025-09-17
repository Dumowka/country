package guru.qa.country.controller;

import guru.qa.country.domain.Country;
import guru.qa.country.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/country")
public class CountryController {

    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/all")
    public List<Country> all() {
        return countryService.allCountries();
    }

    @GetMapping("/{iso}")
    public Country byIso(@PathVariable("iso") String iso) {
        return countryService.countryByIso(iso);
    }

    @PostMapping("/add")
    public Country add(@RequestBody Country country) {
        return countryService.addCountry(country);
    }

    @PatchMapping("/{iso}")
    public Country update(@PathVariable String iso, @RequestBody Country country) {
        return countryService.updateCountry(iso, country);
    }
}
