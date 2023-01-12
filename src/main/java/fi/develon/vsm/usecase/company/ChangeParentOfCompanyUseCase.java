package fi.develon.vsm.usecase.company;

import fi.develon.vsm.domain.core.dto.ChangeParentOfCompanyRequest;
import fi.develon.vsm.domain.core.dto.ChangeParentOfCompanyResponse;
import io.vavr.control.Try;

public interface ChangeParentOfCompanyUseCase {

    Try<ChangeParentOfCompanyResponse> changeParent(ChangeParentOfCompanyRequest request);

}
