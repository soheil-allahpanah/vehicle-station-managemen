package fi.develon.vsm.usecase.impl.station;

import fi.develon.vsm.domain.core.dto.RenameStationRequest;
import fi.develon.vsm.domain.core.dto.RenameStationResponse;
import fi.develon.vsm.domain.core.entity.Station;
import fi.develon.vsm.domain.repository.StationRepository;
import fi.develon.vsm.usecase.exception.DuplicateStationByNameException;
import fi.develon.vsm.usecase.exception.StationNotFoundException;
import fi.develon.vsm.usecase.station.RenameStationUseCase;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
public class RenameStationUseCaseImpl implements RenameStationUseCase {

    StationRepository stationRepository;

    public RenameStationUseCaseImpl(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    public Try<RenameStationResponse> rename(RenameStationRequest request) {
        log.info("rename station with id {} to {}", request.getId().value(), request.getNewName().name());
        Optional<Station> stationOpt = stationRepository.findById(request.getId());
        if (stationOpt.isEmpty()) {
            log.debug("station not found with id {}", request.getId().value());
            return Try.failure(new StationNotFoundException("no station found by given id", 404));
        }
        Optional<Station> stationByNameOpt = stationRepository.findByName(request.getNewName());
        if (stationByNameOpt.isPresent()) {
            log.debug("station with id {} with same name {} exist", stationByNameOpt.get().getStationId().value(), request.getNewName().name());
            return Try.failure(new DuplicateStationByNameException("station with same name exist", 409));
        }
        stationOpt.get().setName(request.getNewName());
        stationOpt.get().setUpdatedAt(LocalDateTime.now());
        var savedStation  = stationRepository.save(stationOpt.get());
        log.info("rename station with id {} to {}", stationOpt.get().getStationId().value(), stationOpt.get().getName().name());
        return Try.success(RenameStationResponse.builder()
                .id(savedStation.getStationId())
                .newName(savedStation.getName())
                .build()
        );
    }
}
