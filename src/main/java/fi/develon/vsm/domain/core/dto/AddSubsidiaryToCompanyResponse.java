package fi.develon.vsm.domain.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import fi.develon.vsm.domain.core.entity.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddSubsidiaryToCompanyResponse {
    private CompanyName name;
    private IdentificationNumber identificationNumber;
    private CompanyName subsidiaryName;
    private IdentificationNumber subsidiaryIdentificationNumber;
    private LocalDateTime updatedAt;
}
