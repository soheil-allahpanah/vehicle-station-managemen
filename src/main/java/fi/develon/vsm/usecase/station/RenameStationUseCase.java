package fi.develon.vsm.usecase.station;

import fi.develon.vsm.domain.core.dto.RenameStationRequest;
import fi.develon.vsm.domain.core.dto.RenameStationResponse;
import io.vavr.control.Try;

public interface RenameStationUseCase {

    Try<RenameStationResponse> rename(RenameStationRequest request);

}
