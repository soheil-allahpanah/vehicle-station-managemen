package fi.develon.vsm.domain.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import fi.develon.vsm.domain.core.entity.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCompanyRequest {
    private CompanyName name;
    private IdentificationNumber identificationNumber;
    private IdentificationNumber parentIdentificationNumber;
}
