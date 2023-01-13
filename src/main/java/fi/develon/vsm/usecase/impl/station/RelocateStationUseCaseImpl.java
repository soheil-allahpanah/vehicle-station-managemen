package fi.develon.vsm.usecase.impl.station;

import fi.develon.vsm.domain.core.dto.RelocateStationRequest;
import fi.develon.vsm.domain.core.dto.RelocateStationResponse;
import fi.develon.vsm.domain.core.entity.Station;
import fi.develon.vsm.domain.repository.StationRepository;
import fi.develon.vsm.usecase.exception.DuplicateStationByLocationException;
import fi.develon.vsm.usecase.exception.StationNotFoundException;
import fi.develon.vsm.usecase.station.RelocateStationUseCase;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class RelocateStationUseCaseImpl implements RelocateStationUseCase {

    StationRepository stationRepository;

    public RelocateStationUseCaseImpl(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }
    @Override
    public Try<RelocateStationResponse> relocate(RelocateStationRequest request) {
        log.info("relocate station {} to {}", request.getId().value(), request.getNewLocation());
        Optional<Station> stationOpt = stationRepository.findById(request.getId());
        if (stationOpt.isEmpty()) {
            log.debug("station with id {} not found", request.getId().value());
            return Try.failure(new StationNotFoundException("no station found by given id", 404));
        }
        Optional<Station> stationByLocationOpt = stationRepository.findByLocation(request.getNewLocation());
        if (stationByLocationOpt.isPresent()) {
            log.debug("station with id {} with same location {} exist", stationByLocationOpt.get().getStationId().value(), request.getNewLocation());
            return Try.failure(new DuplicateStationByLocationException("station with same location exist", 409));
        }
        stationOpt.get().setLocation(request.getNewLocation());
        var savedStation  = stationRepository.save(stationOpt.get());
        log.debug("location of station with id {} change to {}", request.getId().value(), request.getNewLocation());
        return Try.success(RelocateStationResponse.builder()
                .id(savedStation.getStationId())
                .newLocation(savedStation.getLocation())
                .build()
        );
    }
}
