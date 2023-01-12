package fi.develon.vsm.usecase.company;

import fi.develon.vsm.domain.core.dto.GetCompanySubsidiariesRequest;
import fi.develon.vsm.domain.core.dto.GetCompanySubsidiariesResponse;
import io.vavr.control.Try;

public interface GetCompanySubsidiariesUseCase {

    Try<GetCompanySubsidiariesResponse> getSubsidiaries(GetCompanySubsidiariesRequest request);

}
