package fi.develon.vsm.usecase.impl;

import fi.develon.vsm.domain.core.dto.UnregisterCompanyRequest;
import fi.develon.vsm.domain.core.dto.UnregisterCompanyResponse;
import fi.develon.vsm.usecase.UnregisterCompanyUseCase;
import io.vavr.control.Try;

public class UnregisterCompanyUseCaseImpl implements UnregisterCompanyUseCase {

    public Try<UnregisterCompanyResponse> unregister(UnregisterCompanyRequest request) {
      return null;
    }

}
