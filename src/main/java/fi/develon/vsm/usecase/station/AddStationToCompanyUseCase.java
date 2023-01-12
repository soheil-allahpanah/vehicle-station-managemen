package fi.develon.vsm.usecase.station;

import fi.develon.vsm.domain.core.dto.AddStationToCompanyRequest;
import fi.develon.vsm.domain.core.dto.AddStationToCompanyResponse;
import fi.develon.vsm.domain.core.dto.ChangeParentOfCompanyRequest;
import fi.develon.vsm.domain.core.dto.ChangeParentOfCompanyResponse;
import io.vavr.control.Try;

public interface AddStationToCompanyUseCase {

    Try<AddStationToCompanyResponse> addStation(AddStationToCompanyRequest request);

}
