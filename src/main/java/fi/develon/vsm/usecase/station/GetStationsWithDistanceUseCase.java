package fi.develon.vsm.usecase.station;

import fi.develon.vsm.domain.core.dto.GetStationsWithDistanceRequest;
import fi.develon.vsm.domain.core.dto.GetStationsWithDistanceResponse;
import io.vavr.control.Try;

import java.util.List;

public interface GetStationsWithDistanceUseCase {

    Try<List<GetStationsWithDistanceResponse>> fetch(GetStationsWithDistanceRequest request);
}
