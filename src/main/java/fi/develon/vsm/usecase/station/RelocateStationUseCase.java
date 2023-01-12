package fi.develon.vsm.usecase.station;

import fi.develon.vsm.domain.core.dto.RelocateStationRequest;
import fi.develon.vsm.domain.core.dto.RelocateStationResponse;
import io.vavr.control.Try;

public interface RelocateStationUseCase {

    Try<RelocateStationResponse> relocate(RelocateStationRequest request);

}
