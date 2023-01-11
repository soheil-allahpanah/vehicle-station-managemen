package fi.develon.vsm.domain.core.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    private CompanyId id;
    private CompanyId parent;
    private IdentificationNumber identificationNumber;
    private CompanyName name;
    private List<Company> subsidiaries;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

