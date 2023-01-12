package fi.develon.vsm.usecase.impl.station;

import fi.develon.vsm.domain.core.dto.RelocateStationRequest;
import fi.develon.vsm.domain.core.dto.RelocateStationResponse;
import fi.develon.vsm.domain.core.entity.Station;
import fi.develon.vsm.domain.repository.StationRepository;
import fi.develon.vsm.usecase.exception.StationNotFoundException;
import fi.develon.vsm.usecase.station.RelocateStationUseCase;
import io.vavr.control.Try;

import java.util.Optional;

public class RelocateStationUseCaseImpl implements RelocateStationUseCase {

    StationRepository stationRepository;

    public RelocateStationUseCaseImpl(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }
    @Override
    public Try<RelocateStationResponse> relocate(RelocateStationRequest request) {
        Optional<Station> stationOpt = stationRepository.findById(request.getId());
        if (stationOpt.isEmpty()) {
            return Try.failure(new StationNotFoundException("no station found by given id", 404));
        }
        stationOpt.get().setLocation(request.getNewLocation());
        var savedStation  = stationRepository.save(stationOpt.get());
        return Try.success(RelocateStationResponse.builder()
                .id(savedStation.getStationId())
                .newLocation(savedStation.getLocation())
                .build()
        );
    }
}
