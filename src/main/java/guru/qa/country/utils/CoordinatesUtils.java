package guru.qa.country.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.grpc.country.CountryResponse;
import guru.qa.grpc.country.Geometry;
import guru.qa.grpc.country.Line;
import guru.qa.grpc.country.Point;
import guru.qa.grpc.country.Polygon;

import java.util.ArrayList;
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

    public static Geometry dtoToProto(List<List<List<List<Double>>>> dto) {
        Geometry.Builder geomB = Geometry.newBuilder();

        if (dto == null) {
            return geomB.build();
        }

        for (List<List<List<Double>>> polygonDto : dto) {
            Polygon.Builder polyB = Polygon.newBuilder();
            if (polygonDto == null) {
                geomB.addPolygons(polyB);
                continue;
            }

            for (List<List<Double>> lineDto : polygonDto) {
                Line.Builder lineB = Line.newBuilder();
                if (lineDto == null) {
                    polyB.addLines(lineB);
                    continue;
                }

                for (List<Double> pointDto : lineDto) {
                    Point.Builder pointB = Point.newBuilder();
                    if (pointDto != null) {
                        for (Double coord : pointDto) {
                            pointB.addCoordinates(coord == null ? 0.0 : coord);
                        }
                    }
                    lineB.addPoints(pointB.build());
                }

                polyB.addLines(lineB.build());
            }

            geomB.addPolygons(polyB.build());
        }
        return geomB.build();
    }

    public static List<List<List<List<Double>>>> protoToDto(Geometry geometry) {
        List<List<List<List<Double>>>> dto = new ArrayList<>();

        if (geometry == null) return dto;

        for (Polygon polygon : geometry.getPolygonsList()) {
            List<List<List<Double>>> polygonDto = new ArrayList<>();

            for (Line line : polygon.getLinesList()) {
                List<List<Double>> lineDto = new ArrayList<>();

                for (Point point : line.getPointsList()) {
                    List<Double> pointDto = new ArrayList<>();
                    for (double coord : point.getCoordinatesList()) {
                        pointDto.add(coord);
                    }
                    lineDto.add(pointDto);
                }

                polygonDto.add(lineDto);
            }

            dto.add(polygonDto);
        }

        return dto;
    }
}
