package fi.develon.vsm.domain.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import fi.develon.vsm.domain.core.entity.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RenameCompanyRequest {
    private IdentificationNumber identificationNumber;
    private CompanyName newName;
}
