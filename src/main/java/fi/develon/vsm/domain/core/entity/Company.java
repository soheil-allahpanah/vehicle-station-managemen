package fi.develon.vsm.domain.core.entity;



import java.time.LocalDateTime;
import java.util.List;

public record Company(CompanyId id
        , CompanyId parent
        , IdentificationNumber identificationNumber
        , CompanyName name
        , List<Company> subsidiaries
        , LocalDateTime createdAt
        , LocalDateTime updatedAt) {}
