package fi.develon.vsm.domain.core.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import fi.develon.vsm.domain.core.entity.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RenameCompanyResponse {
    private CompanyName name;
    private IdentificationNumber identificationNumber;
    private LocalDateTime updatedAt;
}
