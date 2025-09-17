package guru.qa.country.ex;

public class CountryWithIsoAlreadyExist extends RuntimeException {
    public CountryWithIsoAlreadyExist(String message) {
        super(message);
    }
}
