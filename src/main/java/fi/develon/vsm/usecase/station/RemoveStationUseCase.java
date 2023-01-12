package fi.develon.vsm.usecase.station;

import fi.develon.vsm.domain.core.dto.RemoveStationRequest;
import fi.develon.vsm.domain.core.dto.RemoveStationResponse;
import io.vavr.control.Try;

public interface RemoveStationUseCase {

    Try<RemoveStationResponse> remove(RemoveStationRequest request);

}
