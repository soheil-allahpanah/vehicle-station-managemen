package fi.develon.vsm.domain.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import fi.develon.vsm.domain.core.entity.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeParentOfCompanyResponse {
    private CompanyName name;
    private IdentificationNumber identificationNumber;
    private CompanyName parentName;
    private IdentificationNumber parentIdentificationNumber;
    private LocalDateTime updatedAt;
}
