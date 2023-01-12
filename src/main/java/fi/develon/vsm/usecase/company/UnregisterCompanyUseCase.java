package fi.develon.vsm.usecase.company;

import fi.develon.vsm.domain.core.dto.UnregisterCompanyRequest;
import fi.develon.vsm.domain.core.dto.UnregisterCompanyResponse;
import io.vavr.control.Try;

public interface UnregisterCompanyUseCase {

    Try<UnregisterCompanyResponse> unregister(UnregisterCompanyRequest request);

}
