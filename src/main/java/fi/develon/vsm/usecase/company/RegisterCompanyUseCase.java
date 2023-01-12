package fi.develon.vsm.usecase.company;

import fi.develon.vsm.domain.core.dto.RegisterCompanyRequest;
import fi.develon.vsm.domain.core.dto.RegisterCompanyResponse;
import io.vavr.control.Try;

public interface RegisterCompanyUseCase {

    Try<RegisterCompanyResponse> register(RegisterCompanyRequest request);

}
