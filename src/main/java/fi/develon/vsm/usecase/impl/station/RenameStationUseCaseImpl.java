package fi.develon.vsm.usecase.impl.station;

import fi.develon.vsm.domain.core.dto.RenameStationRequest;
import fi.develon.vsm.domain.core.dto.RenameStationResponse;
import fi.develon.vsm.domain.core.entity.Station;
import fi.develon.vsm.domain.repository.StationRepository;
import fi.develon.vsm.usecase.exception.StationNotFoundException;
import fi.develon.vsm.usecase.station.RenameStationUseCase;
import io.vavr.control.Try;

import java.util.Optional;

public class RenameStationUseCaseImpl implements RenameStationUseCase {

    StationRepository stationRepository;

    public RenameStationUseCaseImpl(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    public Try<RenameStationResponse> rename(RenameStationRequest request) {
        Optional<Station> stationOpt = stationRepository.findById(request.getId());
        if (stationOpt.isEmpty()) {
            return Try.failure(new StationNotFoundException("no station found by given id", 404));
        }
        stationOpt.get().setName(request.getNewName());
        var savedStation  = stationRepository.save(stationOpt.get());
        return Try.success(RenameStationResponse.builder()
                .id(savedStation.getStationId())
                .newName(savedStation.getName())
                .build()
        );
    }
}
