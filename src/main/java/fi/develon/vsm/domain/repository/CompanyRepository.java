package fi.develon.vsm.domain.repository;

import fi.develon.vsm.domain.core.entity.*;

import java.util.Optional;

public interface CompanyRepository {
    Optional<Company> findByIdentifierNumber(IdentificationNumber idNumber);

    Optional<Company> findByName(CompanyName name);

    Company save(Company company);
}
