package fi.develon.vsm.domain.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import fi.develon.vsm.domain.core.entity.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnregisterCompanyResponse {
    private IdentificationNumber identificationNumber;
    private List<Company> unregisteredSubsidiaries;
}
