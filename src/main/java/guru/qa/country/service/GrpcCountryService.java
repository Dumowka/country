package guru.qa.country.service;

import com.google.protobuf.Empty;
import guru.qa.country.domain.graphql.CountryGql;
import guru.qa.country.domain.grpc.CountryGrpc;
import guru.qa.grpc.country.AddedCountriesResponse;
import guru.qa.grpc.country.CountryListResponse;
import guru.qa.grpc.country.CountryRequest;
import guru.qa.grpc.country.CountryResponse;
import guru.qa.grpc.country.CountryServiceGrpc;
import guru.qa.grpc.country.CountryServiceGrpc.CountryServiceImplBase;
import guru.qa.grpc.country.CountryUpdateRequest;
import guru.qa.grpc.country.IsoCountry;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static guru.qa.country.utils.CoordinatesUtils.dtoToProto;
import static guru.qa.country.utils.CoordinatesUtils.parseCoordinates;
import static guru.qa.country.utils.CoordinatesUtils.protoToDto;

@Service
public class GrpcCountryService extends CountryServiceImplBase {

    private final CountryService countryService;

    @Autowired
    public GrpcCountryService(CountryService countryService) {
        this.countryService = countryService;
    }

    @Override
    public void allCountry(Empty request, StreamObserver<CountryListResponse> responseObserver) {
        List<CountryGrpc> countries = countryService.allGrpcCountries();
        responseObserver.onNext(
                CountryListResponse.newBuilder()
                        .addAllCountries(
                                countries.stream().map(country -> CountryResponse.newBuilder()
                                        .setId(country.id().toString())
                                        .setName(country.name())
                                        .setIso(country.iso())
                                        .setCoordinates(dtoToProto(country.coordinates()))
                                        .build()
                                ).toList()
                        )
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void country(IsoCountry request, StreamObserver<CountryResponse> responseObserver) {
        CountryGrpc country = countryService.countryGrpcByIso(request.getIso());
        responseObserver.onNext(
                CountryResponse.newBuilder()
                        .setId(country.id().toString())
                        .setName(country.name())
                        .setIso(country.iso())
                        .setCoordinates(dtoToProto(country.coordinates()))
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<CountryRequest> addCountries(StreamObserver<AddedCountriesResponse> responseObserver) {
        return new StreamObserver<CountryRequest>() {
            private int added = 0;

            @Override
            public void onNext(CountryRequest countryRequest) {
                countryService.addCountry(
                        new CountryGrpc(
                                null,
                                countryRequest.getName(),
                                countryRequest.getIso(),
                                protoToDto(countryRequest.getCoordinates())
                        )
                );
                added++;
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onCompleted() {
                AddedCountriesResponse resp = AddedCountriesResponse.newBuilder()
                        .setCount(added)
                        .build();
                responseObserver.onNext(resp);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void updateCountry(CountryUpdateRequest request, StreamObserver<CountryResponse> responseObserver) {
        CountryGrpc country = countryService.updateCountryGrpc(request.getIso(), protoToDto(request.getCoordinates()));
        responseObserver.onNext(
                CountryResponse.newBuilder()
                        .setId(country.id().toString())
                        .setName(country.name())
                        .setIso(country.iso())
                        .setCoordinates(dtoToProto(country.coordinates()))
                        .build()
        );
        responseObserver.onCompleted();
    }
}
