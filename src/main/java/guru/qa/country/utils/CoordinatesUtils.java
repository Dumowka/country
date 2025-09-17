package guru.qa.country.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class CoordinatesUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static List<List<List<List<Double>>>> parseCoordinates(String coordinates) {
        try {
            return MAPPER.readValue(
                    coordinates,
                    new TypeReference<>() {}
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
