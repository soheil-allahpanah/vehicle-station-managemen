package fi.develon.vsm.domain.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import fi.develon.vsm.domain.core.entity.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnregisterCompanyResponse {
    private IdentificationNumber identificationNumber;
    private List<IdentificationNumber> unregisteredSubsidiariesIdentificationNumber;
}
