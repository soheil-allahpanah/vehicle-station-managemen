package fi.develon.vsm.domain.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import fi.develon.vsm.domain.core.entity.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeParentOfCompanyRequest {
    private IdentificationNumber identificationNumber;
    private IdentificationNumber currentParentIdentificationNumber;
    private IdentificationNumber newParentIdentificationNumber;
}
