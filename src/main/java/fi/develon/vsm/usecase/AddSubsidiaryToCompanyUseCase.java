package fi.develon.vsm.usecase;

import fi.develon.vsm.domain.core.dto.AddSubsidiaryToCompanyRequest;
import fi.develon.vsm.domain.core.dto.AddSubsidiaryToCompanyResponse;
import fi.develon.vsm.domain.core.dto.RenameCompanyRequest;
import fi.develon.vsm.domain.core.dto.RenameCompanyResponse;
import io.vavr.control.Try;

public interface AddSubsidiaryToCompanyUseCase {

    Try<AddSubsidiaryToCompanyResponse> addSubsidiary(AddSubsidiaryToCompanyRequest request);

}
