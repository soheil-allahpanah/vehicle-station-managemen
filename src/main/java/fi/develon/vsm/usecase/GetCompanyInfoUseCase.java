package fi.develon.vsm.usecase;

import fi.develon.vsm.domain.core.dto.GetCompanyInfoRequest;
import fi.develon.vsm.domain.core.dto.GetCompanyInfoResponse;
import fi.develon.vsm.domain.core.dto.UnregisterCompanyRequest;
import fi.develon.vsm.domain.core.dto.UnregisterCompanyResponse;
import io.vavr.control.Try;

public interface GetCompanyInfoUseCase {

    Try<GetCompanyInfoResponse> unregister(GetCompanyInfoRequest request);

}
