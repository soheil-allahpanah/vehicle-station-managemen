package fi.develon.vsm.usecase.impl.station;

import fi.develon.vsm.domain.core.dto.GetStationsWithDistanceRequest;
import fi.develon.vsm.domain.core.dto.GetStationsWithDistanceResponse;
import fi.develon.vsm.domain.repository.StationRepository;
import fi.develon.vsm.usecase.station.GetStationsWithDistanceUseCase;
import io.vavr.control.Try;

import java.util.List;

public class GetStationsWithDistanceUseCaseImpl implements GetStationsWithDistanceUseCase {

    StationRepository stationRepository;

    public GetStationsWithDistanceUseCaseImpl(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    public Try<List<GetStationsWithDistanceResponse>> fetch(GetStationsWithDistanceRequest request) {
        var res = stationRepository.getStationWithDistanceForGivenCompany(request.getIdentificationNumber()
                , request.getLocation());
        if (request.getRadius() != null) {
            res = res.stream().filter(r -> r.getDistance().longValue() <= request.getRadius() * 1000).toList();
        }
        return Try.success(res);
    }
}
