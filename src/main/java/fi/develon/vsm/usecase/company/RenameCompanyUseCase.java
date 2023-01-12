package fi.develon.vsm.usecase.company;

import fi.develon.vsm.domain.core.dto.RenameCompanyRequest;
import fi.develon.vsm.domain.core.dto.RenameCompanyResponse;
import io.vavr.control.Try;

public interface RenameCompanyUseCase {

    Try<RenameCompanyResponse> rename(RenameCompanyRequest request);

}
