package fi.develon.vsm.usecase.impl.station;

import fi.develon.vsm.domain.core.dto.GetStationsWithDistanceRequest;
import fi.develon.vsm.domain.core.dto.GetStationsWithDistanceResponse;
import fi.develon.vsm.domain.repository.StationRepository;
import fi.develon.vsm.usecase.station.GetStationsWithDistanceUseCase;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class GetStationsWithDistanceUseCaseImpl implements GetStationsWithDistanceUseCase {

    StationRepository stationRepository;

    public GetStationsWithDistanceUseCaseImpl(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    public Try<List<GetStationsWithDistanceResponse>> fetch(GetStationsWithDistanceRequest request) {
        log.info("fetch all stations which somehow are belong to {} with their distance to {}"
                , request.getIdentificationNumber().value()
                , request.getLocation());
        var res = stationRepository.getStationWithDistanceForGivenCompany(request.getIdentificationNumber()
                , request.getLocation());
        if (request.getRadius() != null) {
            log.info("station should be in {} km radius"
                    , request.getRadius());
            res = res.stream().filter(r -> r.getDistance().longValue() <= request.getRadius() * 1000).toList();
        }
        return Try.success(res);
    }
}
